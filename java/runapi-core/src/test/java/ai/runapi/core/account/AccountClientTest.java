package ai.runapi.core.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class AccountClientTest {
  @Test
  void infoSendsCorrectRequestAndDecodesResponse() {
    CapturingTransport transport =
        new CapturingTransport(
            "{\"id\":1,\"name\":\"test\",\"email\":\"test@example.com\",\"account\":{\"id\":2,\"name\":\"acme\",\"tier\":\"team\"},\"custom\":\"kept\"}");
    AccountClient client = new AccountClient(transport, options(), false);

    AccountInfoResponse response = client.info(RequestOptions.builder().header("X-Test", "yes").build());

    assertEquals(HttpMethod.GET, transport.request.getMethod());
    assertEquals("/api/v1/me", transport.request.getPath());
    assertEquals("yes", transport.request.getOptions().getHeaders().get("X-Test"));
    assertEquals("test@example.com", response.getEmail());
    assertEquals("acme", response.getAccount().getName());
    assertEquals("team", response.getAccount().extraFields().get("tier").asText());
    assertEquals("kept", response.extraFields().get("custom").asText());
  }

  @Test
  void balanceSendsCorrectRequestAndDecodesResponse() {
    CapturingTransport transport =
        new CapturingTransport(
            "{\"balance_cents\":5000,\"paid_balance_cents\":4000,\"bonus_balance_cents\":1000,\"spent_cents_today\":100,\"spent_cents_total\":2000,\"custom\":true}");
    AccountClient client = new AccountClient(transport, options(), false);

    AccountBalanceResponse response = client.balance();

    assertEquals(HttpMethod.GET, transport.request.getMethod());
    assertEquals("/api/v1/me/balance", transport.request.getPath());
    assertEquals(5000, response.getBalanceCents());
    assertEquals(4000, response.getPaidBalanceCents());
    assertEquals(1000, response.getBonusBalanceCents());
    assertEquals(100, response.getSpentCentsToday());
    assertEquals(2000, response.getSpentCentsTotal());
    assertEquals(true, response.extraFields().get("custom").asBoolean());
  }

  @Test
  void builderWithCustomTransportDoesNotCloseTransport() {
    CapturingTransport transport =
        new CapturingTransport("{\"balance_cents\":0,\"paid_balance_cents\":0,\"bonus_balance_cents\":0,\"spent_cents_today\":0,\"spent_cents_total\":0}");
    AccountClient client =
        AccountClient.builder()
            .apiKey("sk-test")
            .baseUrl(URI.create("https://runapi.ai"))
            .timeout(Duration.ofSeconds(10))
            .maxRetries(1)
            .retryBaseDelay(Duration.ofMillis(10))
            .retryMaxDelay(Duration.ofMillis(20))
            .header("X-Test", "yes")
            .transport(transport)
            .build();

    client.close();

    assertFalse(transport.closed);
  }

  private static ClientOptions options() {
    return ClientOptions.builder().apiKey("sk-test").build();
  }

  private static final class CapturingTransport implements HttpTransport {
    private final String body;
    private HttpRequest request;
    private boolean closed;

    private CapturingTransport(String body) {
      this.body = body;
    }

    @Override
    public HttpResponse send(HttpRequest request) {
      this.request = request;
      return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
    }

    @Override
    public void close() {
      closed = true;
    }
  }
}
