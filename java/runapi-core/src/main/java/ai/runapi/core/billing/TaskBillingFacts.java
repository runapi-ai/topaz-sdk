package ai.runapi.core.billing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Persisted reservation, settlement, and refund facts for a task. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TaskBillingFacts {
  @JsonProperty("reservation") private Reservation reservation;
  @JsonProperty("settlement") private Settlement settlement;
  @JsonProperty("refund") private Refund refund;

  public Reservation getReservation() { return reservation; }
  public Settlement getSettlement() { return settlement; }
  public Refund getRefund() { return refund; }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Reservation {
    @JsonProperty("amount_cents") private Long amountCents;
    public Long getAmountCents() { return amountCents; }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Settlement {
    @JsonProperty("charged_amount_cents") private Long chargedAmountCents;
    @JsonProperty("amount_micro_cents") private Long amountMicroCents;
    public Long getChargedAmountCents() { return chargedAmountCents; }
    public Long getAmountMicroCents() { return amountMicroCents; }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Refund {
    @JsonProperty("refunded_at") private String refundedAt;
    public String getRefundedAt() { return refundedAt; }
  }
}
