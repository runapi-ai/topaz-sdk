package ai.runapi.core.pricing;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
/** Live Price Schedule response, including ETag revalidation status. */
public final class PriceScheduleResponse {
  @JsonProperty("as_of") private String asOf;
  @JsonProperty("price_schedules") private List<PriceSchedule> priceSchedules;
  private String etag; private boolean notModified;
  public static PriceScheduleResponse notModified(String etag) { PriceScheduleResponse value = new PriceScheduleResponse(); value.etag = etag; value.notModified = true; value.priceSchedules = Collections.emptyList(); return value; }
  public String getAsOf() { return asOf; } public List<PriceSchedule> getPriceSchedules() { return priceSchedules == null ? Collections.<PriceSchedule>emptyList() : Collections.unmodifiableList(priceSchedules); } public String getEtag() { return etag; } public boolean isNotModified() { return notModified; } void setEtag(String value) { etag = value; }
}
