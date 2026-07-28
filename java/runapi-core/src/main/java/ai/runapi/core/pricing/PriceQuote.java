package ai.runapi.core.pricing;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/** Runtime reservation estimate for a prospective request. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PriceQuote { @JsonProperty("service") private String service; @JsonProperty("action") private String action; @JsonProperty("model") private String model; @JsonProperty("pricing_status") private String pricingStatus; @JsonProperty("currency") private String currency; @JsonProperty("reservation_amount_cents") private Long reservationAmountCents; @JsonProperty("estimate_basis") private String estimateBasis; @JsonProperty("as_of") private String asOf; public String getService() { return service; } public String getAction() { return action; } public String getModel() { return model; } public String getPricingStatus() { return pricingStatus; } public String getCurrency() { return currency; } public Long getReservationAmountCents() { return reservationAmountCents; } public String getEstimateBasis() { return estimateBasis; } public String getAsOf() { return asOf; } }
