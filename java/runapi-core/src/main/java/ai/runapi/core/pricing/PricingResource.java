package ai.runapi.core.pricing;

import ai.runapi.core.ApiRequestExecutor;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.RunApiException;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Reads live Price Schedules and creates reservation quotes. */
public final class PricingResource {
  private final ApiRequestExecutor executor;

  public PricingResource(HttpTransport transport, ClientOptions options) {
    this.executor = new ApiRequestExecutor(transport, options);
  }

  public PriceScheduleResponse list(PriceScheduleListParams params) {
    HttpRequest.Builder request = HttpRequest.builder(HttpMethod.GET, "/api/v1/price_schedules");
    if (params.getService() != null) request.query("service", params.getService());
    if (params.getAction() != null) request.query("action", params.getAction());
    if (params.getModel() != null) request.query("model", params.getModel());
    if (params.getIfNoneMatch() != null) request.header("If-None-Match", params.getIfNoneMatch()).allowNotModified();
    return decodeSchedule(executor.send(request.build()));
  }

  public PriceQuote createQuote(PriceQuoteRequest quote, RequestOptions options) {
    HttpRequest request = HttpRequest.builder(HttpMethod.POST, "/api/v1/price_quotes")
        .body(new JsonRequestBody(quote.toMap())).options(options).build();
    HttpResponse response = executor.send(request);
    try {
      PriceQuote decodedQuote = Json.mapper().readValue(response.getBody(), PriceQuoteEnvelope.class).getPriceQuote();
      if (decodedQuote == null) {
        throw new IOException("price_quote is required");
      }
      return decodedQuote;
    } catch (IOException error) {
      throw decodeError(response, error);
    }
  }

  private PriceScheduleResponse decodeSchedule(HttpResponse response) {
    if (response.getStatusCode() == 304) return PriceScheduleResponse.notModified(response.firstHeader("ETag"));
    try {
      PriceScheduleResponse decoded = Json.mapper().readValue(response.getBody(), PriceScheduleResponse.class);
      decoded.setEtag(response.firstHeader("ETag"));
      return decoded;
    } catch (IOException error) {
      throw decodeError(response, error);
    }
  }

  private static RunApiException decodeError(HttpResponse response, IOException error) {
    return new RunApiException(
        "Failed to decode response", "decode_error", response.getStatusCode(), null, response.getBody(), error);
  }

  public static final class PriceScheduleListParams {
    private final String service; private final String action; private final String model; private final String ifNoneMatch;
    private PriceScheduleListParams(Builder builder) { service = builder.service; action = builder.action; model = builder.model; ifNoneMatch = builder.ifNoneMatch; }
    public static Builder builder() { return new Builder(); }
    public String getService() { return service; } public String getAction() { return action; } public String getModel() { return model; } public String getIfNoneMatch() { return ifNoneMatch; }
    public static final class Builder { private String service; private String action; private String model; private String ifNoneMatch;
      public Builder service(String value) { service = value; return this; } public Builder action(String value) { action = value; return this; } public Builder model(String value) { model = value; return this; } public Builder ifNoneMatch(String value) { ifNoneMatch = value; return this; } public PriceScheduleListParams build() { return new PriceScheduleListParams(this); } }
  }

  public static final class PriceQuoteRequest {
    private final String service; private final String action; private final String model; private final Map<String, Object> params;
    public PriceQuoteRequest(String service, String action, String model, Map<String, Object> params) { this.service = service; this.action = action; this.model = model; this.params = params == null ? Collections.<String, Object>emptyMap() : new LinkedHashMap<String, Object>(params); }
    Map<String, Object> toMap() { Map<String, Object> value = new LinkedHashMap<String, Object>(); value.put("service", service); value.put("action", action); value.put("model", model); value.put("params", params); return value; }
  }
}
