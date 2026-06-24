package ai.runapi.core.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RequestBodyTest {
  @TempDir Path tempDir;

  @Test
  void writesJsonBody() throws Exception {
    Map<String, Object> payload = new LinkedHashMap<String, Object>();
    payload.put("prompt", "hello");

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    new JsonRequestBody(payload).writeTo(out);

    JsonNode json = new ObjectMapper().readTree(out.toByteArray());
    assertEquals("hello", json.get("prompt").asText());
  }

  @Test
  void writesMultipartBodyFromPath() throws Exception {
    Path file = tempDir.resolve("input.txt");
    Files.write(file, "hello".getBytes(StandardCharsets.UTF_8));

    MultipartRequestBody body =
        MultipartRequestBody.builder()
            .field("file_name", "input.txt")
            .file("file", file, "input.txt", "text/plain")
            .build();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    String multipart = new String(out.toByteArray(), StandardCharsets.UTF_8);

    assertTrue(multipart.contains("name=\"file_name\""));
    assertTrue(multipart.contains("filename=\"input.txt\""));
    assertTrue(multipart.contains("Content-Type: text/plain"));
    assertTrue(multipart.contains("hello"));
  }

  @Test
  void multipartPreservesNonAsciiNames() throws Exception {
    Path file = tempDir.resolve("input.txt");
    Files.write(file, "hi".getBytes(StandardCharsets.UTF_8));

    // Built from code points (U+56FE U+7247 = a CJK filename; U+00E9 = an accented
    // field name) so the source stays pure ASCII and independent of compile encoding.
    String fileName = new String(Character.toChars(0x56FE)) + new String(Character.toChars(0x7247)) + ".png";
    String fieldName = "d" + new String(Character.toChars(0x00E9)) + "but";
    MultipartRequestBody body =
        MultipartRequestBody.builder()
            .field(fieldName, "x")
            .file("file", file, fileName, "image/png")
            .build();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    String multipart = new String(out.toByteArray(), StandardCharsets.UTF_8);

    // With US-ASCII header encoding these would have been mangled to '?'.
    assertTrue(multipart.contains("filename=\"" + fileName + "\""));
    assertTrue(multipart.contains("name=\"" + fieldName + "\""));
  }
}
