package ai.runapi.topaz.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.topaz.types.CompletedUpscaleVideoResponse;
import ai.runapi.topaz.types.UpscaleVideoParams;
import ai.runapi.topaz.types.UpscaleVideoResponse;

/** Upscale Video operations. */
public final class UpscaleVideoResource extends TopazResource {
  /** API endpoint path for upscale video operations. */
  public static final String ENDPOINT = "/api/v1/topaz/upscale_video";

  /** Creates a resource bound to the supplied transport and client options. */
  public UpscaleVideoResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Creates a upscale video task. */
  public TaskCreateResponse create(UpscaleVideoParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a upscale video task with per-request options. */
  public TaskCreateResponse create(UpscaleVideoParams params, RequestOptions options) {
    return createTask(params.action(), params.toMap(), options);
  }

  /** Retrieves a upscale video task by ID. */
  public UpscaleVideoResponse get(String id) {
    return get(id, RequestOptions.none());
  }

  /** Retrieves a upscale video task by ID with per-request options. */
  public UpscaleVideoResponse get(String id, RequestOptions options) {
    return getTask(id, options, UpscaleVideoResponse.class);
  }

  /** Creates a upscale video task and polls until it completes. */
  public CompletedUpscaleVideoResponse run(UpscaleVideoParams params) {
    return run(params, RequestOptions.none());
  }

  /** Creates a upscale video task with per-request options and polls until it completes. */
  public CompletedUpscaleVideoResponse run(UpscaleVideoParams params, RequestOptions options) {
    return runTask(params.action(), params.toMap(), options, UpscaleVideoResponse.class, CompletedUpscaleVideoResponse.class);
  }
}
