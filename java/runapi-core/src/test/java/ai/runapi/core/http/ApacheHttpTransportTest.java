package ai.runapi.core.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.ValidationException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApacheHttpTransportTest {
  private HttpServer server;
  private CapturedRequest captured;

  @BeforeEach
  void startServer() throws Exception {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext(
        "/api/v1/test",
        exchange -> {
          captured = CapturedRequest.from(exchange);
          write(exchange, 200, "{\"ok\":true}");
        });
    server.createContext(
        "/api/v1/error",
        exchange -> {
          captured = CapturedRequest.from(exchange);
          write(exchange, 422, "{\"error\":{\"message\":\"Invalid request\"}}");
        });
    server.createContext(
        "/api/v1/latin1",
        exchange -> {
          // U+00E9 ('é') encoded as ISO-8859-1 (single byte 0xE9), declared in the
          // Content-Type. Decoding as UTF-8 would corrupt it.
          byte[] bytes =
              ("{\"note\":\"caf" + new String(Character.toChars(0x00E9)) + "\"}")
                  .getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
          exchange.getResponseHeaders().set("Content-Type", "application/json; charset=ISO-8859-1");
          exchange.sendResponseHeaders(200, bytes.length);
          try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
          }
        });
    server.start();
  }

  @AfterEach
  void stopServer() {
    server.stop(0);
  }

  @Test
  void sendsManagedHeadersAndJsonBody() {
    ClientOptions options =
        ClientOptions.builder()
            .apiKey("sk-test")
            .baseUrl("http://127.0.0.1:" + server.getAddress().getPort())
            .header("authorization", "wrong")
            .header("X-Custom", "client")
            .build();

    try (ApacheHttpTransport transport = new ApacheHttpTransport(options)) {
      Map<String, String> payload = new LinkedHashMap<String, String>();
      payload.put("prompt", "hello");
      HttpResponse response =
          transport.send(
              HttpRequest.builder(HttpMethod.POST, "/api/v1/test")
                  .body(new JsonRequestBody(payload))
                  .options(RequestOptions.builder().header("x-custom", "request").build())
                  .build());

      assertEquals(200, response.getStatusCode());
      assertEquals("Bearer sk-test", captured.header("Authorization"));
      assertEquals("application/json", captured.header("Accept"));
      assertEquals("runapi-sdk-java/0.1.0", captured.header("User-Agent"));
      assertTrue(captured.header("Content-Type").startsWith("application/json"));
      assertEquals("request", captured.header("X-Custom"));
      assertEquals("{\"prompt\":\"hello\"}", captured.body);
    }
  }

  @Test
  void mapsHttpErrors() {
    ClientOptions options =
        ClientOptions.builder()
            .apiKey("sk-test")
            .baseUrl("http://127.0.0.1:" + server.getAddress().getPort())
            .build();

    try (ApacheHttpTransport transport = new ApacheHttpTransport(options)) {
      try {
        transport.send(HttpRequest.builder(HttpMethod.GET, "/api/v1/error").build());
        fail("expected error");
      } catch (RuntimeException error) {
        ValidationException validation = assertInstanceOf(ValidationException.class, error);
        assertEquals("Invalid request", validation.getMessage());
        assertEquals(422, validation.getStatusCode());
      }
    }
  }

  @Test
  void decodesResponseUsingDeclaredCharset() {
    ClientOptions options =
        ClientOptions.builder()
            .apiKey("sk-test")
            .baseUrl("http://127.0.0.1:" + server.getAddress().getPort())
            .build();

    try (ApacheHttpTransport transport = new ApacheHttpTransport(options)) {
      HttpResponse response =
          transport.send(HttpRequest.builder(HttpMethod.GET, "/api/v1/latin1").build());

      assertEquals(200, response.getStatusCode());
      // "café" must round-trip; a hardcoded UTF-8 decode would yield a replacement char.
      assertTrue(response.getBody().contains("caf" + new String(Character.toChars(0x00E9))));
    }
  }

  @Test
  @SuppressWarnings("deprecation")
  void requestConfigAppliesTimeoutToConnectResponseAndPoolWait() {
    RequestConfig config = ApacheHttpTransport.requestConfig(Duration.ofSeconds(3));

    assertEquals(3000, config.getConnectTimeout().toMilliseconds());
    assertEquals(3000, config.getResponseTimeout().toMilliseconds());
    assertEquals(3000, config.getConnectionRequestTimeout().toMilliseconds());
  }

  @Test
  void connectionConfigAppliesTimeoutToTcpConnect() {
    ConnectionConfig config = ApacheHttpTransport.connectionConfig(Duration.ofSeconds(3));

    assertEquals(3000, config.getConnectTimeout().toMilliseconds());
  }

  private static void write(HttpExchange exchange, int status, String body) throws IOException {
    byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().set("Content-Type", "application/json");
    exchange.sendResponseHeaders(status, bytes.length);
    try (OutputStream out = exchange.getResponseBody()) {
      out.write(bytes);
    }
  }

  private static final class CapturedRequest {
    private final Map<String, String> headers;
    private final String body;

    private CapturedRequest(Map<String, String> headers, String body) {
      this.headers = headers;
      this.body = body;
    }

    static CapturedRequest from(HttpExchange exchange) throws IOException {
      Map<String, String> headers = new LinkedHashMap<String, String>();
      for (Map.Entry<String, java.util.List<String>> entry : exchange.getRequestHeaders().entrySet()) {
        headers.put(entry.getKey(), entry.getValue().isEmpty() ? "" : entry.getValue().get(0));
      }
      byte[] body = readAll(exchange);
      return new CapturedRequest(headers, new String(body, StandardCharsets.UTF_8));
    }

    String header(String name) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        if (entry.getKey().equalsIgnoreCase(name)) {
          return entry.getValue();
        }
      }
      return null;
    }

    private static byte[] readAll(HttpExchange exchange) throws IOException {
      java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int read;
      while ((read = exchange.getRequestBody().read(buffer)) != -1) {
        out.write(buffer, 0, read);
      }
      return out.toByteArray();
    }
  }
}
