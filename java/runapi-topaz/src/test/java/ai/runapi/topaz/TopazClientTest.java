package ai.runapi.topaz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import ai.runapi.topaz.types.CompletedUpscaleImageResponse;
import ai.runapi.topaz.types.UpscaleImageResponse;
import ai.runapi.topaz.types.CompletedUpscaleImageResponse;
import ai.runapi.topaz.types.CompletedUpscaleVideoResponse;
import ai.runapi.topaz.types.UpscaleImageModel;
import ai.runapi.topaz.types.UpscaleImageParams;
import ai.runapi.topaz.types.UpscaleImageResponse;
import ai.runapi.topaz.types.UpscaleVideoModel;
import ai.runapi.topaz.types.UpscaleVideoParams;
import ai.runapi.topaz.types.UpscaleVideoResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class TopazClientTest {
  @Test
  void builderCreatesClientAndUniversalResources() {
    TopazClient client = TopazClient.builder().apiKey("sk-test").build();

    assertNotNull(client.upscaleImage());
    assertNotNull(client.files());
    assertNotNull(client.account());
  }

  @Test
  void openValueClassesSerializeAsScalarStrings() throws Exception {
    String json = Json.mapper().writeValueAsString(new UpscaleImageModel("topaz-upscale-image"));

    assertEquals("\"topaz-upscale-image\"", json);
    assertEquals(new UpscaleImageModel("topaz-upscale-image"), Json.mapper().readValue(json, UpscaleImageModel.class));
  }

  @Test
  void createSendsExpectedRequestShape() throws Exception {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_123\",\"status\":\"processing\"}");
    TopazClient client = TopazClient.builder().apiKey("sk-test").transport(transport).build();

    client.upscaleImage().create(
        UpscaleImageParams.builder()
            .model(UpscaleImageModel.TOPAZ_UPSCALE_IMAGE)
            .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
            .upscaleFactor(1)
            .build()
    );

    assertEquals("POST", transport.request.getMethod().name());
    assertEquals("/api/v1/topaz/upscale_image", transport.request.getPath());
    JsonNode body = bodyJson(transport.request);
    assertNotNull(body);
  }

  @Test
  void getDecodesTaskResponseAndExtraFields() {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_456\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    TopazClient client = TopazClient.builder().apiKey("sk-test").transport(transport).build();

    UpscaleImageResponse response = client.upscaleImage().get("task_456");

    assertEquals("GET", transport.request.getMethod().name());
    assertEquals("/api/v1/topaz/upscale_image/task_456", transport.request.getPath());
    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
  }

  @Test
  void runPollsUntilCompletedAndKeepsExtraFields() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_789\",\"status\":\"processing\"}",
        "{\"id\":\"task_789\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    TopazClient client = TopazClient.builder().apiKey("sk-test").transport(transport).build();

    CompletedUpscaleImageResponse response = client.upscaleImage().run(
        UpscaleImageParams.builder()
            .model(UpscaleImageModel.TOPAZ_UPSCALE_IMAGE)
            .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
            .upscaleFactor(1)
            .build(),
        RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());

    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
    assertEquals(2, transport.calls);
  }

  @Test
  void runRejectsCompletedResponseMissingResultField() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_missing\",\"status\":\"processing\"}",
        "{\"id\":\"task_missing\",\"status\":\"completed\"}");
    TopazClient client = TopazClient.builder().apiKey("sk-test").transport(transport).build();

    assertThrows(
        ValidationException.class,
        () -> client.upscaleImage().run(
                UpscaleImageParams.builder()
                    .model(UpscaleImageModel.TOPAZ_UPSCALE_IMAGE)
                    .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                    .upscaleFactor(1)
                    .build(),
            RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
  }

    @Test
    void coversUpscaleimageResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_upscale_image\",\"status\":\"processing\"}");
      TopazClient createClient = TopazClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.upscaleImage().create(
              UpscaleImageParams.builder()
                  .model(UpscaleImageModel.TOPAZ_UPSCALE_IMAGE)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .upscaleFactor(1)
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_upscale_image_options\",\"status\":\"processing\"}");
      TopazClient createWithOptionsClient = TopazClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.upscaleImage().create(
              UpscaleImageParams.builder()
                  .model(UpscaleImageModel.TOPAZ_UPSCALE_IMAGE)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .upscaleFactor(1)
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_upscale_image\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      TopazClient getClient = TopazClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.upscaleImage().get("task_upscale_image"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_upscale_image_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      TopazClient getWithOptionsClient = TopazClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.upscaleImage().get("task_upscale_image_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_upscale_image_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_upscale_image_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      TopazClient runClient = TopazClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedUpscaleImageResponse runResponse = runClient.upscaleImage().run(
              UpscaleImageParams.builder()
                  .model(UpscaleImageModel.TOPAZ_UPSCALE_IMAGE)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .upscaleFactor(1)
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_upscale_image_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_upscale_image_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      TopazClient runWithOptionsClient = TopazClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.upscaleImage().run(
              UpscaleImageParams.builder()
                  .model(UpscaleImageModel.TOPAZ_UPSCALE_IMAGE)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .upscaleFactor(1)
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversUpscalevideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_upscale_video\",\"status\":\"processing\"}");
      TopazClient createClient = TopazClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.upscaleVideo().create(
              UpscaleVideoParams.builder()
                  .model(UpscaleVideoModel.TOPAZ_UPSCALE_VIDEO)
                  .sourceVideoUrl("https://cdn.runapi.ai/public/samples/video.mp4")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_upscale_video_options\",\"status\":\"processing\"}");
      TopazClient createWithOptionsClient = TopazClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.upscaleVideo().create(
              UpscaleVideoParams.builder()
                  .model(UpscaleVideoModel.TOPAZ_UPSCALE_VIDEO)
                  .sourceVideoUrl("https://cdn.runapi.ai/public/samples/video.mp4")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_upscale_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      TopazClient getClient = TopazClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.upscaleVideo().get("task_upscale_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_upscale_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      TopazClient getWithOptionsClient = TopazClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.upscaleVideo().get("task_upscale_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_upscale_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_upscale_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      TopazClient runClient = TopazClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedUpscaleVideoResponse runResponse = runClient.upscaleVideo().run(
              UpscaleVideoParams.builder()
                  .model(UpscaleVideoModel.TOPAZ_UPSCALE_VIDEO)
                  .sourceVideoUrl("https://cdn.runapi.ai/public/samples/video.mp4")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_upscale_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_upscale_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      TopazClient runWithOptionsClient = TopazClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.upscaleVideo().run(
              UpscaleVideoParams.builder()
                  .model(UpscaleVideoModel.TOPAZ_UPSCALE_VIDEO)
                  .sourceVideoUrl("https://cdn.runapi.ai/public/samples/video.mp4")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

  private static JsonNode bodyJson(HttpRequest request) throws Exception {
    JsonRequestBody body = (JsonRequestBody) request.getBody();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    return Json.mapper().readTree(out.toByteArray());
  }

  private static final class CapturingTransport implements HttpTransport {
    private final String body;
    private HttpRequest request;

    private CapturingTransport(String body) {
      this.body = body;
    }

    public HttpResponse send(HttpRequest request) {
      this.request = request;
      return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }

  private static final class SequenceTransport implements HttpTransport {
    private final String[] responses;
    private int calls;

    private SequenceTransport(String... responses) {
      this.responses = responses;
    }

    public HttpResponse send(HttpRequest request) {
      String response = responses[Math.min(calls, responses.length - 1)];
      calls++;
      return new HttpResponse(200, response, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }
}
