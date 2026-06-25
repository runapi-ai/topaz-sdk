package ai.runapi.core.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.runapi.core.errors.ValidationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ContractValidatorTest {
  @Test
  void generatedContractContainsWanActions() {
    assertTrue(ContractGen.contract().containsKey("wan/text-to-image"));
    assertTrue(ContractGen.contract().get("wan/text-to-image").getModels().contains("wan-2.7-image"));
  }

  @Test
  void rejectsInvalidModel() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("model", "bad-model");

    ValidationException error =
        assertThrows(
            ValidationException.class,
            () -> ContractValidator.validate("wan/text-to-image", params));

    assertTrue(error.getMessage().contains("model must be one of"));
  }

  @Test
  void rejectsInvalidEnumValue() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("model", "wan-2.7-image");
    params.put("aspect_ratio", "bad");

    ValidationException error =
        assertThrows(
            ValidationException.class,
            () -> ContractValidator.validate("wan/text-to-image", params));

    assertTrue(error.getMessage().contains("aspect_ratio must be one of"));
  }

  @Test
  void acceptsValidEnumValue() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("model", "wan-2.7-image");
    params.put("aspect_ratio", "1:1");

    ContractValidator.validate("wan/text-to-image", params);
    assertEquals("1:1", params.get("aspect_ratio"));
  }

  @Test
  void acceptsActionsWithoutModelVariants() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("source_task_id", "task_123");
    params.put("extension_duration_seconds", 6);

    ContractValidator.validate("grok-imagine/extend", params);
  }

  @Test
  void acceptsSingleModelActionsWithoutModelParam() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("descriptions", "Sample character");
    params.put("reference_image_url", "https://example.com/character.png");

    ContractValidator.validate("gemini-omni/create-character", params);
  }

  @Test
  void validatesStringLengthRanges() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("model", "z-image");
    params.put("prompt", "sample");
    params.put("aspect_ratio", "1:1");

    ContractValidator.validate("z-image/text-to-image", params);
  }

  @Test
  void rejectsStringLengthAboveMax() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("model", "z-image");
    params.put("prompt", repeat("a", 1001));
    params.put("aspect_ratio", "1:1");

    ValidationException error =
        assertThrows(
            ValidationException.class,
            () -> ContractValidator.validate("z-image/text-to-image", params));

    assertTrue(error.getMessage().contains("prompt must be between 1 and 1000 characters"));
  }

  @Test
  void countsLengthInCodepointsNotUtf16Units() {
    // 1000 emoji = 1000 codepoints but 2000 UTF-16 units. Counting code units would
    // wrongly reject this prompt as > 1000; codepoint counting matches the other SDKs.
    String emoji = new String(Character.toChars(0x1F600));
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("model", "z-image");
    params.put("prompt", repeat(emoji, 1000));
    params.put("aspect_ratio", "1:1");

    ContractValidator.validate("z-image/text-to-image", params);
  }

  @Test
  void rejectsMissingFieldRequiredByCrossFieldRule() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("model", "suno-v5");
    params.put("vocal_mode", "auto_lyrics");

    ValidationException error =
        assertThrows(
            ValidationException.class,
            () -> ContractValidator.validate("suno/text-to-music", params));

    assertTrue(error.getMessage().contains("prompt is required when vocal_mode is auto_lyrics"));
  }

  @Test
  void rejectsForbiddenFieldFromCrossFieldRule() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("model", "suno-v5");
    params.put("vocal_mode", "auto_lyrics");
    params.put("prompt", "sample");
    params.put("lyrics", "sample lyrics");

    ValidationException error =
        assertThrows(
            ValidationException.class,
            () -> ContractValidator.validate("suno/text-to-music", params));

    assertTrue(error.getMessage().contains("lyrics is not allowed when vocal_mode is auto_lyrics"));
  }

  @Test
  void appliesModelGatedRulesOnlyToTheMatchingModel() {
    Map<String, Object> multilingualParams = new HashMap<String, Object>();
    multilingualParams.put("model", "text-to-speech-multilingual-v2");
    multilingualParams.put("text", "hello");

    ValidationException error =
        assertThrows(
            ValidationException.class,
            () -> ContractValidator.validate("elevenlabs/text-to-speech", multilingualParams));

    assertTrue(error.getMessage().contains("voice is required when model is text-to-speech-multilingual-v2"));

    Map<String, Object> turboParams = new HashMap<String, Object>();
    turboParams.put("model", "text-to-speech-turbo-v2.5");
    turboParams.put("text", "hello");

    ContractValidator.validate("elevenlabs/text-to-speech", turboParams);
  }

  @Test
  void evaluatesModelConditionsAgainstAutoSelectedSingleModel() {
    ContractAction contract =
        new ContractAction(
            ContractBuilders.list("single-model"),
            ContractBuilders.fieldsByModel(
                new Object[][] {{"single-model", ContractBuilders.fields(new Object[][] {})}}),
            ContractBuilders.rulesByModel(
                new Object[][] {
                  {
                    "single-model",
                    ContractBuilders.rules(
                        ContractBuilders.rule(
                            ContractBuilders.conditions(new Object[][] {{"model", "single-model"}}),
                            ContractBuilders.list("required_field"),
                            Collections.<String>emptyList()))
                  }
                }));
    Map<String, Object> params = new HashMap<String, Object>();

    ValidationException error =
        assertThrows(ValidationException.class, () -> ContractValidator.validate(contract, params));

    assertTrue(error.getMessage().contains("required_field is required when model is single-model"));
  }

  private static String repeat(String value, int count) {
    StringBuilder builder = new StringBuilder();
    for (int index = 0; index < count; index++) {
      builder.append(value);
    }
    return builder.toString();
  }
}
