package ai.runapi.core.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.RunApiException;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PricingResourceTest {
  @Test void scheduleRevalidatesAndQuoteUsesCanonicalBody() throws Exception {
    CapturingTransport transport = new CapturingTransport("{\"as_of\":\"2026-07-23T12:00:00.000000Z\",\"price_schedules\":[{\"service\":\"flux\",\"action\":\"text_to_image\",\"pricing_status\":\"available\",\"currency\":\"USD\"}]}", "{\"price_quote\":{\"service\":\"flux\",\"action\":\"text_to_image\",\"pricing_status\":\"available\",\"currency\":\"USD\",\"reservation_amount_cents\":12,\"estimate_basis\":\"exact\"}}");
    PricingResource pricing = new PricingResource(transport, ClientOptions.builder().build());
    PriceScheduleResponse schedules = pricing.list(PricingResource.PriceScheduleListParams.builder().service("flux").ifNoneMatch("\"v1\"").build());
    assertEquals("flux", schedules.getPriceSchedules().get(0).getService());
    assertEquals("\"v2\"", schedules.getEtag());
    assertEquals("\"v1\"", transport.requests.get(0).getHeaders().get("If-None-Match"));
    assertEquals("flux", transport.requests.get(0).getQuery().get("service"));
    assertEquals(null, transport.requests.get(0).getHeaders().get("Authorization"));
    PriceQuote quote = pricing.createQuote(new PricingResource.PriceQuoteRequest("flux", "text_to_image", "flux-2-klein", Collections.<String, Object>singletonMap("prompt", "cube")), RequestOptions.none());
    assertEquals(12, quote.getReservationAmountCents().intValue());
    assertEquals("available", quote.getPricingStatus());
    assertEquals("POST", transport.requests.get(1).getMethod().name());
    assertEquals("/api/v1/price_quotes", transport.requests.get(1).getPath());
    JsonNode body = bodyJson((JsonRequestBody) transport.requests.get(1).getBody());
    assertEquals("flux", body.get("service").asText());
    assertEquals("text_to_image", body.get("action").asText());
    assertEquals("flux-2-klein", body.get("model").asText());
    assertEquals("cube", body.get("params").get("prompt").asText());
  }
  @Test void notModifiedScheduleIsAValidResult() {
    PricingResource pricing = new PricingResource(new HttpTransport() { public HttpResponse send(HttpRequest request) { return new HttpResponse(304, "", Collections.singletonMap("ETag", Collections.singletonList("\"v1\""))); } public void close() {} }, ClientOptions.builder().build());
    assertTrue(pricing.list(PricingResource.PriceScheduleListParams.builder().ifNoneMatch("\"v1\"").build()).isNotModified());
  }
  @Test void decodesAmountsAboveIntegerRange() {
    long amount = (long) Integer.MAX_VALUE + 1L;
    String scheduleBody = "{\"as_of\":\"2026-07-23T12:00:00.000000Z\",\"price_schedules\":[{\"unit_price_cents\":" + amount + ",\"input_price_per_1m_cents\":" + amount + ",\"output_price_per_1m_cents\":" + amount + ",\"cache_read_price_per_1m_cents\":" + amount + ",\"cache_write_price_per_1m_cents\":" + amount + ",\"cache_write_5m_price_per_1m_cents\":" + amount + ",\"cache_write_1h_price_per_1m_cents\":" + amount + "}]}";
    String quoteBody = "{\"price_quote\":{\"service\":\"flux\",\"action\":\"text_to_image\",\"pricing_status\":\"available\",\"currency\":\"USD\",\"reservation_amount_cents\":" + amount + ",\"estimate_basis\":\"exact\"}}";
    PricingResource pricing = new PricingResource(new CapturingTransport(scheduleBody, quoteBody), ClientOptions.builder().build());

    PriceSchedule schedule = pricing.list(PricingResource.PriceScheduleListParams.builder().build()).getPriceSchedules().get(0);
    PriceQuote quote = pricing.createQuote(new PricingResource.PriceQuoteRequest("flux", "text_to_image", null, Collections.<String, Object>emptyMap()), RequestOptions.none());

    assertEquals(Long.valueOf(amount), schedule.getUnitPriceCents());
    assertEquals(Long.valueOf(amount), schedule.getInputPricePer1mCents());
    assertEquals(Long.valueOf(amount), schedule.getOutputPricePer1mCents());
    assertEquals(Long.valueOf(amount), schedule.getCacheReadPricePer1mCents());
    assertEquals(Long.valueOf(amount), schedule.getCacheWritePricePer1mCents());
    assertEquals(Long.valueOf(amount), schedule.getCacheWrite5mPricePer1mCents());
    assertEquals(Long.valueOf(amount), schedule.getCacheWrite1hPricePer1mCents());
    assertEquals(Long.valueOf(amount), quote.getReservationAmountCents());
  }
  @Test void malformedSuccessResponsesUseTheStandardDecodeError() {
    PricingResource pricing = new PricingResource(new CapturingTransport("not-json", "still-not-json", "{}"), ClientOptions.builder().build());

    RunApiException schedule = assertThrows(RunApiException.class, () -> pricing.list(PricingResource.PriceScheduleListParams.builder().build()));
    assertDecodeError(schedule, "not-json");
    RunApiException quote = assertThrows(RunApiException.class, () -> pricing.createQuote(new PricingResource.PriceQuoteRequest("flux", "text_to_image", null, Collections.<String, Object>emptyMap()), RequestOptions.none()));
    assertDecodeError(quote, "still-not-json");
    RunApiException missingQuote = assertThrows(RunApiException.class, () -> pricing.createQuote(new PricingResource.PriceQuoteRequest("flux", "text_to_image", null, Collections.<String, Object>emptyMap()), RequestOptions.none()));
    assertDecodeError(missingQuote, "{}");
  }
  private static void assertDecodeError(RunApiException error, String body) {
    assertEquals("decode_error", error.getCode());
    assertEquals(200, error.getStatusCode());
    assertEquals(body, error.getResponseBody());
  }
  private static JsonNode bodyJson(JsonRequestBody body) throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    return Json.mapper().readTree(out.toByteArray());
  }
  private static final class CapturingTransport implements HttpTransport {
    private final String[] bodies; private int index; private final java.util.List<HttpRequest> requests = new java.util.ArrayList<HttpRequest>();
    CapturingTransport(String... bodies) { this.bodies = bodies; }
    public HttpResponse send(HttpRequest request) { requests.add(request); return new HttpResponse(200, bodies[index++], Collections.singletonMap("ETag", Collections.singletonList("\"v2\""))); }
    public void close() {}
  }
}
