package ai.runapi.core.files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.http.MultipartRequestBody;
import ai.runapi.core.json.Json;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class FilesClientTest {
  @Test
  void createFromUrlSendsCanonicalJsonSource() throws Exception {
    CapturingTransport transport = new CapturingTransport();
    FilesClient client = new FilesClient(transport, options(), false);

    FileUploadResponse response =
        client.create(
            FileCreateParams.fromUrl("https://cdn.runapi.ai/public/samples/image.png").fileName("image.png").build(),
            RequestOptions.builder().header("X-Test", "yes").build());

    assertEquals(HttpMethod.POST, transport.request.getMethod());
    assertEquals("/api/v1/files", transport.request.getPath());
    assertEquals("yes", transport.request.getOptions().getHeaders().get("X-Test"));
    JsonRequestBody body = assertInstanceOf(JsonRequestBody.class, transport.request.getBody());
    JsonNode json = bodyJson(body);
    assertEquals("url", json.get("source").get("type").asText());
    assertEquals("https://cdn.runapi.ai/public/samples/image.png", json.get("source").get("url").asText());
    assertEquals("image.png", json.get("file_name").asText());
    assertEquals("image.png", response.getFileName());
    assertEquals(Instant.parse("2026-06-08T10:30:00Z"), response.getCreatedAt());
    assertEquals("kept", response.extraFields().get("custom").asText());
  }

  @Test
  void createParamsSupportBuilderStyleFileName() throws Exception {
    CapturingTransport transport = new CapturingTransport();
    FilesClient client = new FilesClient(transport, options(), false);

    client.create(FileCreateParams.fromUrl("https://cdn.runapi.ai/public/samples/image.png").fileName("image.png").build());

    JsonNode json = bodyJson(assertInstanceOf(JsonRequestBody.class, transport.request.getBody()));
    assertEquals("image.png", json.get("file_name").asText());
  }

  @Test
  void createFromBase64SendsCanonicalJsonSource() throws Exception {
    CapturingTransport transport = new CapturingTransport();
    FilesClient client = new FilesClient(transport, options(), false);

    client.create(FileCreateParams.fromBase64("aGVsbG8=").build());

    JsonNode json = bodyJson(assertInstanceOf(JsonRequestBody.class, transport.request.getBody()));
    assertEquals("base64", json.get("source").get("type").asText());
    assertEquals("aGVsbG8=", json.get("source").get("data").asText());
    assertFalse(json.has("file_name"));
  }

  @Test
  void createFromPathSendsMultipartStreamingBody() throws Exception {
    CapturingTransport transport = new CapturingTransport();
    FilesClient client = new FilesClient(transport, options(), false);
    Path path = Files.createTempFile("runapi-java-sdk", ".png");
    Files.write(path, "image-bytes".getBytes(StandardCharsets.UTF_8));

    client.create(FileCreateParams.fromPath(path).fileName("custom.png").build());

    MultipartRequestBody body = assertInstanceOf(MultipartRequestBody.class, transport.request.getBody());
    assertTrue(body.contentType().startsWith("multipart/form-data; boundary="));
    // Streams from disk: non-repeatable so the transport pipes writeTo() instead of
    // buffering the whole upload into memory.
    assertFalse(body.isRepeatable());
    assertEquals(null, body.contentLength());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    String multipart = new String(out.toByteArray(), StandardCharsets.UTF_8);
    assertTrue(multipart.contains("name=\"file_name\""));
    assertTrue(multipart.contains("custom.png"));
    assertTrue(multipart.contains("name=\"file\"; filename=\"custom.png\""));
  }

  @Test
  void paramsRejectBlankInputs() {
    assertThrows(NullPointerException.class, () -> FileCreateParams.fromPath(null));
    assertThrows(IllegalArgumentException.class, () -> FileCreateParams.fromUrl(" "));
    assertThrows(IllegalArgumentException.class, () -> FileCreateParams.fromBase64(""));
  }

  @Test
  void builderWithCustomTransportDoesNotCloseTransport() {
    CapturingTransport transport = new CapturingTransport();
    FilesClient client =
        FilesClient.builder()
            .apiKey("sk-test")
            .baseUrl(URI.create("https://api.runapi.ai"))
            .timeout(Duration.ofSeconds(10))
            .maxRetries(1)
            .retryBaseDelay(Duration.ofMillis(10))
            .retryMaxDelay(Duration.ofMillis(20))
            .header("X-Test", "yes")
            .transport(transport)
            .build();

    client.close();

    assertFalse(transport.closed);
  }

  private static JsonNode bodyJson(JsonRequestBody body) throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    return Json.mapper().readTree(out.toByteArray());
  }

  private static ClientOptions options() {
    return ClientOptions.builder().apiKey("sk-test").build();
  }

  private static final class CapturingTransport implements HttpTransport {
    private HttpRequest request;
    private boolean closed;

    @Override
    public HttpResponse send(HttpRequest request) {
      this.request = request;
      return new HttpResponse(
          200,
          "{\"file_name\":\"image.png\",\"url\":\"https://file.runapi.ai/temp/image.png\",\"size_bytes\":204800,\"mime_type\":\"image/png\",\"created_at\":\"2026-06-08T10:30:00Z\",\"expires_at\":\"2026-06-08T11:30:00Z\",\"custom\":\"kept\"}",
          Collections.<String, java.util.List<String>>emptyMap());
    }

    @Override
    public void close() {
      closed = true;
    }
  }
}
