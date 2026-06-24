package ai.runapi.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import ai.runapi.core.account.AccountBalanceResponse;
import ai.runapi.core.account.AccountInfoResponse;
import ai.runapi.core.files.FileCreateParams;
import ai.runapi.core.files.FileUploadResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class LocalRunApiSmokeTest {
  @Test
  void readsAccountFromLocalRunApi() {
    assumeTrue("true".equals(System.getenv("RUNAPI_JAVA_LOCAL_SMOKE")), "local smoke disabled");

    try (BaseClient client = BaseClient.builder().build()) {
      AccountInfoResponse info = client.account().info();
      AccountBalanceResponse balance = client.account().balance();

      assertEquals("demo@example.com", info.getEmail());
      assertNotNull(info.getAccount());
      assertEquals("Demo User", info.getName());
      assertEquals(5000, balance.getBalanceCents());

      FileUploadResponse upload =
          client.files()
              .create(
                  FileCreateParams.fromBase64(
                          Base64.getEncoder().encodeToString("hello from java sdk".getBytes(StandardCharsets.UTF_8)))
                      .fileName("java-smoke.txt")
                      .build());
      assertEquals("java-smoke.txt", upload.getFileName());
      assertEquals("text/plain", upload.getMimeType());
      assertEquals("hello from java sdk".length(), upload.getSizeBytes());
      assertNotNull(upload.getUrl());
    }
  }
}
