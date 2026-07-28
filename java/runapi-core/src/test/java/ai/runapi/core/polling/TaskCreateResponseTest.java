package ai.runapi.core.polling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.runapi.core.json.Json;
import org.junit.jupiter.api.Test;

class TaskCreateResponseTest {
  @Test
  void preservesUnknownFields() throws Exception {
    TaskCreateResponse response =
        Json.mapper().readValue("{\"id\":\"task_123\",\"status\":\"processing\",\"task_replayed\":true,\"custom\":true}", TaskCreateResponse.class);

    assertEquals("task_123", response.getId());
    assertEquals("processing", response.getStatus());
    assertEquals(true, response.getTaskReplayed());
    assertEquals(true, response.extraFields().get("custom").asBoolean());
  }
}
