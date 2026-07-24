package ai.runapi.core.files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.http.RequestBody;
import ai.runapi.core.json.Json;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
  void createFromPathUploadsDirectly() throws Exception {
    DirectTransport transport = new DirectTransport();
    FilesClient client = new FilesClient(transport, options(), false);
    Path path = Files.createTempFile("runapi-java-sdk", ".png");
    Files.write(path, "image-bytes".getBytes(StandardCharsets.UTF_8));

    FileUploadResponse response =
        client.create(FileCreateParams.fromPath(path).fileName("custom.png").build());

    // prepare then confirm; bytes never travel through the API transport
    assertEquals("/api/v1/files/prepare", transport.paths.get(0));
    assertEquals("/api/v1/files/confirm", transport.paths.get(1));
    JsonNode prepare = bodyJson(assertInstanceOf(JsonRequestBody.class, transport.bodies.get(0)));
    assertEquals("custom.png", prepare.get("filename").asText());
    assertEquals(11, prepare.get("byte_size").asInt());
    byte[] digest = MessageDigest.getInstance("MD5").digest("image-bytes".getBytes(StandardCharsets.UTF_8));
    assertEquals(Base64.getEncoder().encodeToString(digest), prepare.get("checksum").asText());

    // bytes went straight to the pre-authorized upload URL via transport.upload
    assertEquals("https://file.runapi.ai/put-target", transport.uploadUrl);
    assertEquals("image-bytes", new String(transport.uploadBody, StandardCharsets.UTF_8));
    assertEquals("application/octet-stream", transport.uploadHeaders.get("Content-Type"));

    JsonNode confirm = bodyJson(assertInstanceOf(JsonRequestBody.class, transport.bodies.get(1)));
    assertEquals("signed-blob-id", confirm.get("signed_id").asText());
    assertEquals("image.png", response.getFileName());
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
            .baseUrl(URI.create("https://runapi.ai"))
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

  /**
   * Returns a prepare response, then a resource for confirm, and records the
   * direct-upload PUT the client routes through transport.upload().
   */
  private static final class DirectTransport implements HttpTransport {
    private final List<String> paths = new ArrayList<String>();
    private final List<RequestBody> bodies = new ArrayList<RequestBody>();
    private String uploadUrl;
    private Map<String, String> uploadHeaders;
    private byte[] uploadBody;

    @Override
    public HttpResponse send(HttpRequest request) {
      paths.add(request.getPath());
      bodies.add(request.getBody());
      String json =
          request.getPath().endsWith("/prepare")
              ? "{\"signed_id\":\"signed-blob-id\",\"upload_url\":\"https://file.runapi.ai/put-target\",\"headers\":{\"Content-Type\":\"application/octet-stream\"}}"
              : "{\"file_name\":\"image.png\",\"url\":\"https://file.runapi.ai/temp/image.png\",\"size_bytes\":11,\"mime_type\":\"image/png\",\"created_at\":\"2026-06-08T10:30:00Z\",\"expires_at\":\"2026-06-08T11:30:00Z\"}";
      return new HttpResponse(200, json, Collections.<String, java.util.List<String>>emptyMap());
    }

    @Override
    public void upload(String url, Map<String, String> headers, byte[] body) {
      uploadUrl = url;
      uploadHeaders = headers;
      uploadBody = body;
    }

    @Override
    public void close() {}
  }
}
