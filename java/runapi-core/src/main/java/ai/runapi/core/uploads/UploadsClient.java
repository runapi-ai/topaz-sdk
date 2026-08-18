package ai.runapi.core.uploads;

import ai.runapi.core.ApiRequestExecutor;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.http.MultipartRequestBody;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Multipart Upload lifecycle operations. */
public final class UploadsClient {
  private static final String ENDPOINT = "/v1/uploads";
  private final ApiRequestExecutor executor;

  public UploadsClient(HttpTransport transport, ClientOptions options) {
    this.executor = new ApiRequestExecutor(transport, options);
  }

  public UploadObject create(long bytes, String filename, String mimeType) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("bytes", bytes);
    body.put("filename", filename);
    body.put("mime_type", mimeType);
    body.put("purpose", "user_data");
    return sendUpload(ENDPOINT, body, RequestOptions.none());
  }

  public UploadPart addPart(String uploadId, Path path) {
    MultipartRequestBody body = MultipartRequestBody.builder()
        .file("data", path, path.getFileName().toString(), null).build();
    HttpRequest request = HttpRequest.builder(HttpMethod.POST, uploadPath(uploadId) + "/parts")
        .body(body).build();
    return executor.send(request, UploadPart.class);
  }

  public UploadObject complete(String uploadId, String... partIds) {
    return sendUpload(uploadPath(uploadId) + "/complete",
        java.util.Collections.<String, Object>singletonMap("part_ids", Arrays.asList(partIds)),
        RequestOptions.none());
  }

  public UploadObject cancel(String uploadId) {
    return sendUpload(uploadPath(uploadId) + "/cancel", java.util.Collections.<String, Object>emptyMap(), RequestOptions.none());
  }

  private UploadObject sendUpload(String path, Map<String, Object> body, RequestOptions options) {
    HttpRequest request = HttpRequest.builder(HttpMethod.POST, path)
        .options(options).body(new JsonRequestBody(body)).build();
    return executor.send(request, UploadObject.class);
  }

  private static String uploadPath(String uploadId) {
    String checked = Objects.requireNonNull(uploadId, "uploadId").trim();
    if (checked.isEmpty()) throw new IllegalArgumentException("uploadId must not be blank");
    try {
      return ENDPOINT + "/" + URLEncoder.encode(checked, "UTF-8").replace("+", "%20");
    } catch (java.io.UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }
}
