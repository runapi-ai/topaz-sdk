package ai.runapi.core.uploads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.http.MultipartRequestBody;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class UploadsClientTest {
  @Test
  void lifecycleUsesCanonicalPathsAndBodies() throws Exception {
    CapturingTransport transport = new CapturingTransport();
    UploadsClient uploads = new UploadsClient(transport, ClientOptions.builder().apiKey("sk-test").build());
    Path part = Files.createTempFile("runapi-part", ".bin");
    Files.write(part, new byte[] {1, 2, 3});

    assertEquals("upload_123", uploads.create(3, "data.bin", "application/octet-stream").getId());
    assertEquals("part_123", uploads.addPart("upload_123", part).getId());
    uploads.complete("upload_123", "part_123");
    uploads.cancel("upload_123");

    assertEquals("/v1/uploads", transport.requests.get(0).getPath());
    assertInstanceOf(JsonRequestBody.class, transport.requests.get(0).getBody());
    assertEquals("/v1/uploads/upload_123/parts", transport.requests.get(1).getPath());
    assertInstanceOf(MultipartRequestBody.class, transport.requests.get(1).getBody());
    assertEquals("/v1/uploads/upload_123/complete", transport.requests.get(2).getPath());
    assertEquals("/v1/uploads/upload_123/cancel", transport.requests.get(3).getPath());
  }

  private static final class CapturingTransport implements HttpTransport {
    private final List<HttpRequest> requests = new ArrayList<HttpRequest>();

    @Override
    public HttpResponse send(HttpRequest request) {
      requests.add(request);
      String body = request.getPath().endsWith("/parts")
          ? "{\"id\":\"part_123\",\"object\":\"upload.part\",\"created_at\":1,\"upload_id\":\"upload_123\"}"
          : "{\"id\":\"upload_123\",\"object\":\"upload\",\"bytes\":3,\"created_at\":1,\"filename\":\"data.bin\",\"purpose\":\"user_data\",\"status\":\"pending\",\"expires_at\":2}";
      return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
    }

    @Override
    public void close() {}
  }
}
