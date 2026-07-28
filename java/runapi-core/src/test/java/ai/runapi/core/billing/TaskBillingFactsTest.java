package ai.runapi.core.billing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.runapi.core.json.Json;
import org.junit.jupiter.api.Test;

class TaskBillingFactsTest {
  @Test void decodesAmountsAboveIntegerRange() throws Exception {
    long amount = (long) Integer.MAX_VALUE + 1L;
    String body = "{\"reservation\":{\"amount_cents\":" + amount + "},\"settlement\":{\"charged_amount_cents\":" + amount + ",\"amount_micro_cents\":" + amount + "}}";

    TaskBillingFacts billing = Json.mapper().readValue(body, TaskBillingFacts.class);

    assertEquals(Long.valueOf(amount), billing.getReservation().getAmountCents());
    assertEquals(Long.valueOf(amount), billing.getSettlement().getChargedAmountCents());
    assertEquals(Long.valueOf(amount), billing.getSettlement().getAmountMicroCents());
  }
}
