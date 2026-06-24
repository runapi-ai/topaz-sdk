package ai.runapi.topaz.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.topaz.types.CompletedUpscaleImageResponse;
import ai.runapi.topaz.types.UpscaleImageParams;
import ai.runapi.topaz.types.UpscaleImageResponse;

/** Upscale Image operations. */
public final class UpscaleImageResource extends TopazResource {
  /** API endpoint path for upscale image operations. */
  public static final String ENDPOINT = "/api/v1/topaz/upscale_image";

  /** Creates a resource bound to the supplied transport and client options. */
  public UpscaleImageResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Creates a upscale image task. */
  public TaskCreateResponse create(UpscaleImageParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a upscale image task with per-request options. */
  public TaskCreateResponse create(UpscaleImageParams params, RequestOptions options) {
    return createTask(params.action(), params.toMap(), options);
  }

  /** Retrieves a upscale image task by ID. */
  public UpscaleImageResponse get(String id) {
    return get(id, RequestOptions.none());
  }

  /** Retrieves a upscale image task by ID with per-request options. */
  public UpscaleImageResponse get(String id, RequestOptions options) {
    return getTask(id, options, UpscaleImageResponse.class);
  }

  /** Creates a upscale image task and polls until it completes. */
  public CompletedUpscaleImageResponse run(UpscaleImageParams params) {
    return run(params, RequestOptions.none());
  }

  /** Creates a upscale image task with per-request options and polls until it completes. */
  public CompletedUpscaleImageResponse run(UpscaleImageParams params, RequestOptions options) {
    return runTask(params.action(), params.toMap(), options, UpscaleImageResponse.class, CompletedUpscaleImageResponse.class);
  }
}
