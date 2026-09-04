package ai.runapi.core.polling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.ApiRequestExecutor;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.errors.TaskFailedException;
import ai.runapi.core.errors.TaskTimeoutException;
import ai.runapi.core.http.ApacheHttpTransport;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class TaskTest {
  @Test
  void subscribesToAcceptedTaskThroughOpaqueLocationWithAnActualHttpServer() throws Exception {
    AtomicInteger resultRequests = new AtomicInteger();
    HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext(
        "/create",
        exchange ->
            respond(
                exchange,
                202,
                "{\"id\":\"tsk_123\",\"status\":\"processing\"}",
                headers(
                    "Location", "http://127.0.0.1:" + server.getAddress().getPort() + "/opaque/result",
                    "Retry-After", "0")));
    server.createContext(
        "/opaque/result",
        exchange -> {
          if (resultRequests.incrementAndGet() == 1) {
            respond(exchange, 200, "{\"id\":\"tsk_123\",\"status\":\"processing\"}", headers("Retry-After", "0"));
          } else {
            respond(
                exchange,
                200,
                "{\"id\":\"tsk_123\",\"status\":\"completed\",\"response\":{\"status\":200,\"content_type\":\"application/json\",\"headers\":{},\"body\":{\"prompts\":[\"Short prompt\"]}}}",
                Collections.<String, String>emptyMap());
          }
        });
    server.start();

    try (ApacheHttpTransport transport = new ApacheHttpTransport(ClientOptions.builder().baseUrl("http://127.0.0.1:" + server.getAddress().getPort()).build())) {
      ClientOptions options = ClientOptions.builder().baseUrl("http://127.0.0.1:" + server.getAddress().getPort()).transport(transport).build();
      Task<Map> task = Task.start(
          new ApiRequestExecutor(transport, options),
          HttpRequest.builder(HttpMethod.POST, "/create").build(),
          Map.class,
          Duration.ofMillis(1),
          Duration.ofSeconds(1));

      Map result = task.subscribe();

      assertEquals("tsk_123", task.getId());
      assertEquals("Short prompt", ((List<?>) result.get("prompts")).get(0));
      assertEquals(2, resultRequests.get());
    } finally {
      server.stop(0);
    }
  }

  @Test
  void preservesTheGeneratedIdempotencyKeyAcrossPostRetries() {
    AtomicInteger calls = new AtomicInteger();
    List<String> keys = new ArrayList<String>();
    HttpTransport transport =
        new StubTransport() {
          @Override
          public HttpResponse send(HttpRequest request) {
            keys.add(request.getHeaders().get("Idempotency-Key"));
            if (calls.incrementAndGet() == 1) {
              throw new ai.runapi.core.errors.NetworkException("temporary", null);
            }
            return response(200, "{\"prompts\":[\"Short prompt\"]}", Collections.<String, String>emptyMap());
          }
        };
    ClientOptions options =
        ClientOptions.builder()
            .retryBaseDelay(Duration.ofMillis(1))
            .retryMaxDelay(Duration.ofMillis(1))
            .build();

    Task<Map> task = Task.start(
        new ApiRequestExecutor(transport, options),
        HttpRequest.builder(HttpMethod.POST, "/create").build(),
        Map.class,
        Duration.ofMillis(1),
        Duration.ofSeconds(1));

    assertNotNull(task.subscribe());
    assertEquals(2, calls.get());
    assertNotNull(keys.get(0));
    assertEquals(keys.get(0), keys.get(1));
  }

  @Test
  void decodesTextSrtAndVttTaskResultsWithoutForcingJson() {
    for (String contentType : Arrays.asList("text/plain", "application/x-subrip", "text/vtt")) {
      Task<String> task = Task.start(
          new ApiRequestExecutor(
              new SequenceTransport(
                  response(202, "{\"id\":\"tsk_text\",\"status\":\"processing\"}", headers("Location", "https://api.example.test/opaque/result")),
                  response(200, "{\"id\":\"tsk_text\",\"status\":\"completed\",\"response\":{\"status\":200,\"content_type\":\"" + contentType + "\",\"headers\":{},\"body\":\"caption body\"}}", Collections.<String, String>emptyMap())),
              ClientOptions.builder().build()),
          HttpRequest.builder(HttpMethod.POST, "/create").build(),
          String.class,
          Duration.ofMillis(1),
          Duration.ofSeconds(1));

      assertEquals("caption body", task.subscribe());
    }
  }

  @Test
  void decodesAudioResultJsonAsTheRequestedEndpointType() {
    Task<Map> task = Task.start(
        new ApiRequestExecutor(
            new SequenceTransport(
                response(202, "{\"id\":\"tsk_audio\",\"status\":\"processing\"}", headers("Location", "https://api.example.test/opaque/result")),
                response(200, "{\"id\":\"tsk_audio\",\"status\":\"completed\",\"response\":{\"status\":200,\"content_type\":\"application/json\",\"headers\":{},\"body\":{\"audios\":[{\"url\":\"https://file.runapi.ai/audio.mp3\"}]}}}", Collections.<String, String>emptyMap())),
            ClientOptions.builder().build()),
        HttpRequest.builder(HttpMethod.POST, "/create").build(),
        Map.class,
        Duration.ofMillis(1),
        Duration.ofSeconds(1));

    Map result = task.subscribe();

    assertEquals("https://file.runapi.ai/audio.mp3", ((Map<?, ?>) ((List<?>) result.get("audios")).get(0)).get("url"));
  }

  @Test
  void raisesTheStableTaskFailureForFailedTaskResults() {
    Task<Map> task = Task.start(
        new ApiRequestExecutor(
            new SequenceTransport(
                response(202, "{\"id\":\"tsk_failed\",\"status\":\"processing\"}", headers("Location", "https://api.example.test/opaque/result")),
                response(200, "{\"id\":\"tsk_failed\",\"status\":\"failed\",\"response\":{\"status\":500,\"content_type\":\"application/json\",\"headers\":{},\"body\":{\"error\":{\"message\":\"Task failed\"}}}}", Collections.<String, String>emptyMap())),
            ClientOptions.builder().build()),
        HttpRequest.builder(HttpMethod.POST, "/create").build(),
        Map.class,
        Duration.ofMillis(1),
        Duration.ofSeconds(1));

    assertThrows(TaskFailedException.class, task::subscribe);
  }

  @Test
  void doesNotPollAfterTheMaximumWaitExpires() {
    AtomicInteger polls = new AtomicInteger();
    Task<Map> task = Task.start(
        new ApiRequestExecutor(
            new StubTransport() {
              @Override
              public HttpResponse send(HttpRequest request) {
                if (request.getMethod() == HttpMethod.GET) {
                  polls.incrementAndGet();
                }
                return response(
                    202,
                    "{\"id\":\"tsk_timeout\",\"status\":\"processing\"}",
                    headers("Location", "https://api.example.test/opaque/result", "Retry-After", "1"));
              }
            },
            ClientOptions.builder().build()),
        HttpRequest.builder(HttpMethod.POST, "/create").build(),
        Map.class,
        Duration.ofMillis(1),
        Duration.ofMillis(50));

    assertThrows(TaskTimeoutException.class, task::subscribe);
    assertEquals(0, polls.get());
  }

  private static HttpResponse response(int status, String body, Map<String, String> headers) {
    Map<String, List<String>> values = new LinkedHashMap<String, List<String>>();
    for (Map.Entry<String, String> header : headers.entrySet()) {
      values.put(header.getKey(), Collections.singletonList(header.getValue()));
    }
    return new HttpResponse(status, body, values);
  }

  private static Map<String, String> headers(String... values) {
    Map<String, String> headers = new LinkedHashMap<String, String>();
    for (int index = 0; index < values.length; index += 2) {
      headers.put(values[index], values[index + 1]);
    }
    return headers;
  }

  private static void respond(HttpExchange exchange, int status, String body, Map<String, String> headers) throws IOException {
    for (Map.Entry<String, String> header : headers.entrySet()) {
      exchange.getResponseHeaders().add(header.getKey(), header.getValue());
    }
    byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(status, bytes.length);
    exchange.getResponseBody().write(bytes);
    exchange.close();
  }

  private abstract static class StubTransport implements HttpTransport {
    @Override
    public void close() {}
  }

  private static final class SequenceTransport extends StubTransport {
    private final HttpResponse[] responses;
    private int index;

    private SequenceTransport(HttpResponse... responses) {
      this.responses = responses;
    }

    @Override
    public HttpResponse send(HttpRequest request) {
      return responses[Math.min(index++, responses.length - 1)];
    }
  }
}
