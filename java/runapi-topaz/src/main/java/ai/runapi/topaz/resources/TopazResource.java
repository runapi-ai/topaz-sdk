package ai.runapi.topaz.resources;

import ai.runapi.core.ApiRequestExecutor;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.contract.ContractValidator;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import ai.runapi.core.polling.Poller;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.core.polling.TaskResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

abstract class TopazResource {
  private final String endpoint;
  private final ApiRequestExecutor executor;
  private final ClientOptions options;

  TopazResource(HttpTransport transport, ClientOptions options, String endpoint) {
    this.endpoint = endpoint;
    this.executor = new ApiRequestExecutor(transport, options);
    this.options = options;
  }

  final TaskCreateResponse createTask(String action, Map<String, Object> body, RequestOptions requestOptions) {
    Objects.requireNonNull(action, "action");
    Objects.requireNonNull(body, "body");
    Objects.requireNonNull(requestOptions, "requestOptions");
    ContractValidator.validate(action, body);
    return executor.send(
        HttpRequest.builder(HttpMethod.POST, endpoint).body(new JsonRequestBody(body)).options(requestOptions).build(),
        TaskCreateResponse.class);
  }

  final <T> T runSync(String action, Map<String, Object> body, RequestOptions requestOptions, Class<T> responseType) {
    Objects.requireNonNull(action, "action");
    Objects.requireNonNull(body, "body");
    Objects.requireNonNull(requestOptions, "requestOptions");
    ContractValidator.validate(action, body);
    return executor.send(
        HttpRequest.builder(HttpMethod.POST, endpoint).body(new JsonRequestBody(body)).options(requestOptions).build(),
        responseType);
  }

  final <T extends TaskResponse> T getTask(String id, RequestOptions requestOptions, Class<T> responseType) {
    String checkedId = requireNonBlank(id, "id");
    return executor.send(
        HttpRequest.builder(HttpMethod.GET, endpoint + "/" + checkedId)
            .options(Objects.requireNonNull(requestOptions, "requestOptions"))
            .build(),
        responseType);
  }

  final <T extends TaskResponse, C extends T> C runTask(
      String action,
      Map<String, Object> body,
      RequestOptions requestOptions,
      Class<T> responseType,
      Class<C> completedType) {
    TaskCreateResponse created = createTask(action, body, requestOptions);
    String id = requireNonBlank(created.getId(), "id");
    T response = Poller.pollUntilComplete(
        () -> getTask(id, requestOptions, responseType),
        pollingInterval(requestOptions),
        pollingMaxWait(requestOptions));
    return Json.mapper().convertValue(response, completedType);
  }

  private static String requireNonBlank(String value, String name) {
    String checked = Objects.requireNonNull(value, name).trim();
    if (checked.isEmpty()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
    return checked;
  }

  private Duration pollingInterval(RequestOptions requestOptions) {
    Duration override = requestOptions.getPollingInterval();
    return override == null ? options.getPollingInterval() : override;
  }

  private Duration pollingMaxWait(RequestOptions requestOptions) {
    Duration override = requestOptions.getPollingMaxWait();
    return override == null ? options.getPollingMaxWait() : override;
  }
}
