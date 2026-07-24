package ai.runapi.core.contract;

import static ai.runapi.core.contract.ContractBuilders.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** SDK contract metadata keyed by action. */
public final class ContractGen {
  private static final Map<String, ContractAction> CONTRACT = build();

  private ContractGen() {}

  /** Returns SDK contract metadata keyed by action. */
  public static Map<String, ContractAction> contract() {
    return CONTRACT;
  }

  private static Map<String, ContractAction> build() {
    Map<String, ContractAction> contract = new LinkedHashMap<String, ContractAction>();
    addActions0(contract);
    addActions1(contract);
    addActions2(contract);
    addActions3(contract);
    addActions4(contract);
    addActions5(contract);
    addActions6(contract);
    addActions7(contract);
    addActions8(contract);
    addActions9(contract);
    addActions10(contract);
    addActions11(contract);
    addActions12(contract);
    addActions13(contract);
    addActions14(contract);
    return Collections.unmodifiableMap(contract);
  }

  private static void addActions0(Map<String, ContractAction> contract) {
contract.put("elevenlabs/isolate-audio", new ContractAction(
    list("audio-isolation"),
          fieldsByModel(new Object[][] {
            {"audio-isolation", fields(new Object[][] {
                    {"callback_url", field()},
                    {"source_audio_url", field(required())},
            })},
          })));
contract.put("elevenlabs/speech-to-text", new ContractAction(
    list("speech-to-text"),
          fieldsByModel(new Object[][] {
            {"speech-to-text", fields(new Object[][] {
                    {"callback_url", field()},
                    {"diarize", field()},
                    {"language_code", field()},
                    {"source_audio_url", field(required())},
                    {"tag_audio_events", field()},
            })},
          })));
contract.put("elevenlabs/text-to-dialogue", new ContractAction(
    list("text-to-dialogue-v3"),
          fieldsByModel(new Object[][] {
            {"text-to-dialogue-v3", fields(new Object[][] {
                    {"callback_url", field()},
                    {"dialogue", field(required())},
                    {"language_code", field()},
                    {"stability", field(enumValues(Double.valueOf(0.0), Double.valueOf(0.5), Double.valueOf(1.0)))},
            })},
          })));
contract.put("elevenlabs/text-to-sound", new ContractAction(
    list("sound-effect-v2"),
          fieldsByModel(new Object[][] {
            {"sound-effect-v2", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"loop", field()},
                    {"output_format", field(enumValues("mp3_22050_32", "mp3_44100_32", "mp3_44100_64", "mp3_44100_96", "mp3_44100_128", "mp3_44100_192", "pcm_8000", "pcm_16000", "pcm_22050", "pcm_24000", "pcm_44100", "pcm_48000", "ulaw_8000", "alaw_8000", "opus_48000_32", "opus_48000_64", "opus_48000_96", "opus_48000_128", "opus_48000_192"))},
                    {"prompt_influence", field()},
                    {"text", field(required())},
            })},
          })));
contract.put("elevenlabs/text-to-speech", new ContractAction(
    list("text-to-speech-multilingual-v2", "text-to-speech-turbo-v2.5"),
          fieldsByModel(new Object[][] {
            {"text-to-speech-multilingual-v2", fields(new Object[][] {
                    {"callback_url", field()},
                    {"language_code", field()},
                    {"model", field()},
                    {"next_text", field()},
                    {"previous_text", field()},
                    {"similarity_boost", field()},
                    {"speed", field()},
                    {"stability", field()},
                    {"style", field()},
                    {"text", field(required())},
                    {"timestamps", field()},
                    {"voice", field()},
            })},
            {"text-to-speech-turbo-v2.5", fields(new Object[][] {
                    {"callback_url", field()},
                    {"language_code", field()},
                    {"model", field()},
                    {"next_text", field()},
                    {"previous_text", field()},
                    {"similarity_boost", field()},
                    {"speed", field()},
                    {"stability", field()},
                    {"style", field()},
                    {"text", field(required())},
                    {"timestamps", field()},
                    {"voice", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"text-to-speech-multilingual-v2", rules(rule(conditions(new Object[][] {{"model", "text-to-speech-multilingual-v2"}}), list("voice"), list()))},
          })));
contract.put("fish-audio/text-to-speech", new ContractAction(
    list("s1", "s2-pro"),
          fieldsByModel(new Object[][] {
            {"s1", fields(new Object[][] {
                    {"model", field()},
                    {"references", field()},
                    {"text", field(required())},
            })},
            {"s2-pro", fields(new Object[][] {
                    {"model", field()},
                    {"references", field()},
                    {"text", field(required())},
            })},
          })));
contract.put("flux-2/remix-image", new ContractAction(
    list("flux-2-flex-remix-image", "flux-2-pro-remix-image"),
          fieldsByModel(new Object[][] {
            {"flux-2-flex-remix-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "3:2", "2:3", "auto"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("1k", "2k"))},
                    {"prompt", field(required())},
                    {"source_image_urls", field(required(), minItems(1), maxItems(8))},
            })},
            {"flux-2-pro-remix-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "3:2", "2:3", "auto"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("1k", "2k"))},
                    {"prompt", field(required())},
                    {"source_image_urls", field(required(), minItems(1), maxItems(8))},
            })},
          })));
contract.put("flux-2/text-to-image", new ContractAction(
    list("flux-2-flex-text-to-image", "flux-2-pro-text-to-image"),
          fieldsByModel(new Object[][] {
            {"flux-2-flex-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("1k", "2k"))},
                    {"prompt", field(required())},
            })},
            {"flux-2-pro-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("1k", "2k"))},
                    {"prompt", field(required())},
            })},
          })));
  }

  private static void addActions1(Map<String, ContractAction> contract) {
contract.put("flux-kontext/text-to-image", new ContractAction(
    list("flux-kontext-max", "flux-kontext-pro"),
          fieldsByModel(new Object[][] {
            {"flux-kontext-max", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("21:9", "16:9", "4:3", "1:1", "3:4", "9:16"))},
                    {"callback_url", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_translation", field()},
                    {"model", field(required())},
                    {"output_format", field(enumValues("jpeg", "png"))},
                    {"prompt", field(required())},
                    {"safety_tolerance", field()},
                    {"source_image_url", field()},
                    {"watermark", field()},
            })},
            {"flux-kontext-pro", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("21:9", "16:9", "4:3", "1:1", "3:4", "9:16"))},
                    {"callback_url", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_translation", field()},
                    {"model", field(required())},
                    {"output_format", field(enumValues("jpeg", "png"))},
                    {"prompt", field(required())},
                    {"safety_tolerance", field()},
                    {"source_image_url", field()},
                    {"watermark", field()},
            })},
          })));
contract.put("flux/remix-image", new ContractAction(
    list("flux-dev", "flux-pro"),
          fieldsByModel(new Object[][] {
            {"flux-dev", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1)))},
                    {"prompt", field(required(), min(Double.valueOf(3.0)), max(Double.valueOf(5000.0)), length())},
                    {"source_image_url", field(required())},
            })},
            {"flux-pro", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1)))},
                    {"prompt", field(required(), min(Double.valueOf(3.0)), max(Double.valueOf(5000.0)), length())},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("flux/text-to-image", new ContractAction(
    list("flux-2-klein", "flux-dev", "flux-pro"),
          fieldsByModel(new Object[][] {
            {"flux-2-klein", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1)))},
                    {"prompt", field(required(), min(Double.valueOf(3.0)), max(Double.valueOf(5000.0)), length())},
            })},
            {"flux-dev", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1)))},
                    {"prompt", field(required(), min(Double.valueOf(3.0)), max(Double.valueOf(5000.0)), length())},
            })},
            {"flux-pro", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1)))},
                    {"prompt", field(required(), min(Double.valueOf(3.0)), max(Double.valueOf(5000.0)), length())},
            })},
          })));
contract.put("gemini-omni/create-audio", new ContractAction(
    list("gemini-omni-audio"),
          fieldsByModel(new Object[][] {
            {"gemini-omni-audio", fields(new Object[][] {
                    {"audio_id", field(required())},
                    {"example_dialogue", field()},
                    {"name", field(required())},
                    {"voice_description", field()},
            })},
          })));
contract.put("gemini-omni/create-character", new ContractAction(
    list("gemini-omni-character"),
          fieldsByModel(new Object[][] {
            {"gemini-omni-character", fields(new Object[][] {
                    {"audio_ids", field()},
                    {"character_name", field()},
                    {"descriptions", field(required())},
                    {"reference_image_url", field(required())},
            })},
          })));
contract.put("gemini-omni/text-to-video", new ContractAction(
    list("gemini-omni-flash-preview", "gemini-omni-text-to-video"),
          fieldsByModel(new Object[][] {
            {"gemini-omni-flash-preview", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16"))},
                    {"audio_ids", field()},
                    {"callback_url", field()},
                    {"character_ids", field()},
                    {"duration_seconds", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p"))},
                    {"prompt", field(required())},
                    {"reference_image_urls", field()},
                    {"seed", field()},
                    {"video_list", field()},
            })},
            {"gemini-omni-text-to-video", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16"))},
                    {"audio_ids", field(maxItems(3))},
                    {"callback_url", field()},
                    {"character_ids", field(maxItems(3))},
                    {"duration_seconds", field(required(), enumValues(Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(8), Integer.valueOf(10)))},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p", "4k"))},
                    {"prompt", field(required())},
                    {"reference_image_urls", field(maxItems(7))},
                    {"seed", field()},
                    {"video_list", field(maxItems(1))},
            })},
          }),
          rulesByModel(new Object[][] {
{"gemini-omni-flash-preview", rules(rule(conditions(new Object[][] {{"model", "gemini-omni-flash-preview"}}), list(), list("reference_image_urls", "audio_ids", "video_list", "character_ids", "duration_seconds", "seed")))},
          })));
contract.put("gemini-tts/text-to-speech", new ContractAction(
    list("gemini-2.5-pro-tts", "gemini-3.1-flash-tts"),
          fieldsByModel(new Object[][] {
            {"gemini-2.5-pro-tts", fields(new Object[][] {
                    {"callback_url", field()},
                    {"dialogue_turns", field(required(), minItems(1))},
                    {"model", field(required())},
                    {"sample_context", field()},
                    {"scene", field()},
                    {"speakers", field(required(), minItems(1))},
                    {"temperature", field(min(Double.valueOf(0.0)), max(Double.valueOf(2.0)))},
            })},
            {"gemini-3.1-flash-tts", fields(new Object[][] {
                    {"callback_url", field()},
                    {"dialogue_turns", field(required(), minItems(1))},
                    {"model", field(required())},
                    {"sample_context", field()},
                    {"scene", field()},
                    {"speakers", field(required(), minItems(1))},
                    {"temperature", field(min(Double.valueOf(0.0)), max(Double.valueOf(2.0)))},
            })},
          })));
contract.put("gpt-4o-image/text-to-image", new ContractAction(
    list("gpt-4o-image"),
          fieldsByModel(new Object[][] {
            {"gpt-4o-image", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"enable_prompt_expansion", field()},
                    {"mask_url", field()},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(4)))},
                    {"prompt", field()},
                    {"source_image_urls", field(maxItems(5))},
            })},
          })));
  }

  private static void addActions2(Map<String, ContractAction> contract) {
contract.put("gpt-image-2/edit-image", new ContractAction(
    list("gpt-image-2"),
          fieldsByModel(new Object[][] {
            {"gpt-image-2", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("auto", "1:1", "3:2", "2:3", "4:3", "3:4", "5:4", "4:5", "16:9", "9:16", "2:1", "1:2", "3:1", "1:3", "21:9", "9:21"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("1k", "2k", "4k"))},
                    {"prompt", field(required())},
                    {"source_image_urls", field(required())},
            })},
          })));
contract.put("gpt-image-2/text-to-image", new ContractAction(
    list("gpt-image-2"),
          fieldsByModel(new Object[][] {
            {"gpt-image-2", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("auto", "1:1", "3:2", "2:3", "4:3", "3:4", "5:4", "4:5", "16:9", "9:16", "2:1", "1:2", "3:1", "1:3", "21:9", "9:21"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("1k", "2k", "4k"))},
                    {"prompt", field(required())},
            })},
          })));
contract.put("gpt-image/edit-image", new ContractAction(
    list("gpt-image-1.5"),
          fieldsByModel(new Object[][] {
            {"gpt-image-1.5", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "2:3", "3:2"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"prompt", field(required())},
                    {"quality", field(required(), enumValues("medium", "high"))},
                    {"source_image_urls", field(required())},
            })},
          })));
contract.put("gpt-image/text-to-image", new ContractAction(
    list("gpt-image-1.5"),
          fieldsByModel(new Object[][] {
            {"gpt-image-1.5", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "2:3", "3:2"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"prompt", field(required())},
                    {"quality", field(required(), enumValues("medium", "high"))},
            })},
          })));
contract.put("grok-imagine/edit-image", new ContractAction(
    list("grok-imagine-edit-image"),
          fieldsByModel(new Object[][] {
            {"grok-imagine-edit-image", fields(new Object[][] {
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"prompt", field()},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("grok-imagine/extend", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"extension_duration_seconds", field(enumValues(Integer.valueOf(6), Integer.valueOf(10)))},
                    {"prompt", field()},
                    {"source_task_id", field()},
                    {"start_seconds", field()},
            })},
          })));
contract.put("grok-imagine/image-to-video", new ContractAction(
    list("grok-imagine-image-to-video", "grok-imagine-video-1.5-fast", "grok-imagine-video-1.5-preview"),
          fieldsByModel(new Object[][] {
            {"grok-imagine-image-to-video", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("2:3", "3:2", "1:1", "16:9", "9:16"))},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_safety_checker", field()},
                    {"index", field()},
                    {"model", field()},
                    {"motion_style", field(enumValues("fun", "normal", "spicy"))},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
                    {"source_image_url", field()},
                    {"source_task_id", field()},
            })},
            {"grok-imagine-video-1.5-fast", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "16:9", "9:16", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(min(Double.valueOf(1.0)), max(Double.valueOf(30.0)))},
                    {"enable_safety_checker", field()},
                    {"index", field()},
                    {"model", field()},
                    {"motion_style", field()},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
                    {"source_image_url", field(required())},
                    {"source_task_id", field()},
            })},
            {"grok-imagine-video-1.5-preview", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "16:9", "9:16", "3:2", "2:3", "auto"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(min(Double.valueOf(1.0)), max(Double.valueOf(15.0)))},
                    {"enable_safety_checker", field()},
                    {"index", field()},
                    {"model", field()},
                    {"motion_style", field()},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field(min(Double.valueOf(1.0)), max(Double.valueOf(4096.0)), length())},
                    {"reference_image_urls", field()},
                    {"source_image_url", field(required())},
                    {"source_task_id", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"grok-imagine-image-to-video", rules(rule(conditions(new Object[][] {{"model", "grok-imagine-image-to-video"}}), list(), list("reference_image_urls")))},
{"grok-imagine-video-1.5-fast", rules(rule(conditions(new Object[][] {{"model", "grok-imagine-video-1.5-fast"}}), list(), list("source_task_id", "index", "motion_style", "enable_safety_checker")))},
{"grok-imagine-video-1.5-preview", rules(rule(conditions(new Object[][] {{"model", "grok-imagine-video-1.5-preview"}}), list(), list("source_task_id", "index", "reference_image_urls", "motion_style", "enable_safety_checker")))},
          })));
contract.put("grok-imagine/text-to-image", new ContractAction(
    list("grok-imagine-text-to-image"),
          fieldsByModel(new Object[][] {
            {"grok-imagine-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("2:3", "3:2", "1:1", "16:9", "9:16"))},
                    {"callback_url", field()},
                    {"enable_pro", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"prompt", field()},
            })},
          })));
  }

  private static void addActions3(Map<String, ContractAction> contract) {
contract.put("grok-imagine/text-to-video", new ContractAction(
    list("grok-imagine-text-to-video", "grok-imagine-video-1.5-fast", "grok-imagine-video-1.5-preview"),
          fieldsByModel(new Object[][] {
            {"grok-imagine-text-to-video", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("2:3", "3:2", "1:1", "16:9", "9:16"))},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"motion_style", field(enumValues("fun", "normal", "spicy"))},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
            })},
            {"grok-imagine-video-1.5-fast", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "16:9", "9:16", "3:2", "2:3"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(min(Double.valueOf(1.0)), max(Double.valueOf(30.0)))},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"motion_style", field()},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field(required())},
                    {"reference_image_urls", field()},
            })},
            {"grok-imagine-video-1.5-preview", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "16:9", "9:16", "3:2", "2:3", "auto"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(min(Double.valueOf(1.0)), max(Double.valueOf(15.0)))},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"motion_style", field()},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(4096.0)), length())},
                    {"reference_image_urls", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"grok-imagine-text-to-video", rules(rule(conditions(new Object[][] {{"model", "grok-imagine-text-to-video"}}), list(), list("reference_image_urls")))},
{"grok-imagine-video-1.5-fast", rules(rule(conditions(new Object[][] {{"model", "grok-imagine-video-1.5-fast"}}), list(), list("motion_style", "enable_safety_checker")))},
{"grok-imagine-video-1.5-preview", rules(rule(conditions(new Object[][] {{"model", "grok-imagine-video-1.5-preview"}}), list(), list("reference_image_urls", "motion_style", "enable_safety_checker")))},
          })));
contract.put("grok-imagine/upscale-image", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"source_task_id", field()},
            })},
          })));
contract.put("hailuo/image-to-video", new ContractAction(
    list("hailuo-02-image-to-video-pro", "hailuo-02-image-to-video-standard", "hailuo-2.3-image-to-video-pro", "hailuo-2.3-image-to-video-standard"),
          fieldsByModel(new Object[][] {
            {"hailuo-02-image-to-video-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"output_resolution", field()},
                    {"prompt", field()},
                    {"prompt_optimizer", field()},
            })},
            {"hailuo-02-image-to-video-standard", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(6), Integer.valueOf(10)))},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("512p", "768p"))},
                    {"prompt", field()},
                    {"prompt_optimizer", field()},
            })},
            {"hailuo-2.3-image-to-video-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(6), Integer.valueOf(10)))},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("768p", "1080p"))},
                    {"prompt", field()},
                    {"prompt_optimizer", field()},
            })},
            {"hailuo-2.3-image-to-video-standard", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(6), Integer.valueOf(10)))},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("768p", "1080p"))},
                    {"prompt", field()},
                    {"prompt_optimizer", field()},
            })},
          })));
contract.put("hailuo/text-to-video", new ContractAction(
    list("hailuo-02-text-to-video-pro", "hailuo-02-text-to-video-standard"),
          fieldsByModel(new Object[][] {
            {"hailuo-02-text-to-video-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"prompt", field()},
                    {"prompt_optimizer", field()},
            })},
            {"hailuo-02-text-to-video-standard", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(6), Integer.valueOf(10)))},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"prompt", field()},
                    {"prompt_optimizer", field()},
            })},
          })));
contract.put("happyhorse/edit-video", new ContractAction(
    list("happyhorse-edit-video"),
          fieldsByModel(new Object[][] {
            {"happyhorse-edit-video", fields(new Object[][] {
                    {"audio_setting", field(enumValues("auto", "original"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"reference_image_urls", field(maxItems(5))},
                    {"seed", field()},
                    {"source_video_url", field(required())},
            })},
          })));
contract.put("happyhorse/image-to-video", new ContractAction(
    list("happyhorse-1.0-i2v", "happyhorse-image-to-video"),
          fieldsByModel(new Object[][] {
            {"happyhorse-1.0-i2v", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"first_frame_image_url", field(required())},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"seed", field()},
            })},
            {"happyhorse-image-to-video", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"first_frame_image_url", field(required())},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"seed", field()},
            })},
          })));
contract.put("happyhorse/text-to-video", new ContractAction(
    list("happyhorse-1.0-r2v", "happyhorse-1.0-t2v", "happyhorse-character", "happyhorse-text-to-video"),
          fieldsByModel(new Object[][] {
            {"happyhorse-1.0-r2v", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1", "4:3", "3:4"))},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"reference_image_urls", field(required(), minItems(1), maxItems(9))},
                    {"seed", field()},
            })},
            {"happyhorse-1.0-t2v", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1", "4:3", "3:4"))},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
                    {"seed", field()},
            })},
            {"happyhorse-character", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1", "4:3", "3:4"))},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"reference_image_urls", field(required(), minItems(1), maxItems(9))},
                    {"seed", field()},
            })},
            {"happyhorse-text-to-video", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1", "4:3", "3:4"))},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
                    {"seed", field()},
            })},
          })));
contract.put("ideogram-v3/edit-image", new ContractAction(
    list("ideogram-v3-character-edit", "ideogram-v3-edit"),
          fieldsByModel(new Object[][] {
            {"ideogram-v3-character-edit", fields(new Object[][] {
                    {"callback_url", field()},
                    {"enable_prompt_expansion", field()},
                    {"mask_url", field(required())},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)))},
                    {"prompt", field()},
                    {"reference_image_urls", field(required())},
                    {"rendering_speed", field(enumValues("turbo", "balanced", "quality"))},
                    {"seed", field()},
                    {"source_image_url", field(required())},
                    {"style", field(enumValues("auto", "realistic", "fiction"))},
            })},
            {"ideogram-v3-edit", fields(new Object[][] {
                    {"callback_url", field()},
                    {"enable_prompt_expansion", field()},
                    {"mask_url", field(required())},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
                    {"rendering_speed", field(enumValues("turbo", "balanced", "quality"))},
                    {"seed", field()},
                    {"source_image_url", field(required())},
                    {"style", field()},
            })},
          })));
  }

  private static void addActions4(Map<String, ContractAction> contract) {
contract.put("ideogram-v3/reframe-image", new ContractAction(
    list("ideogram-v3-reframe"),
          fieldsByModel(new Object[][] {
            {"ideogram-v3-reframe", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "3:4", "9:16", "4:3", "16:9"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)))},
                    {"rendering_speed", field(enumValues("turbo", "balanced", "quality"))},
                    {"seed", field()},
                    {"source_image_url", field(required())},
                    {"style", field(enumValues("auto", "general", "realistic", "design"))},
            })},
          })));
contract.put("ideogram-v3/remix-image", new ContractAction(
    list("ideogram-v3-character-remix", "ideogram-v3-remix"),
          fieldsByModel(new Object[][] {
            {"ideogram-v3-character-remix", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "3:4", "9:16", "4:3", "16:9"))},
                    {"callback_url", field()},
                    {"enable_prompt_expansion", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)))},
                    {"prompt", field()},
                    {"reference_image_urls", field(required())},
                    {"reference_mask_urls", field()},
                    {"rendering_speed", field(enumValues("turbo", "balanced", "quality"))},
                    {"seed", field()},
                    {"source_image_url", field(required())},
                    {"strength", field()},
                    {"style", field(enumValues("auto", "realistic", "fiction"))},
                    {"style_reference_image_urls", field()},
            })},
            {"ideogram-v3-remix", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "3:4", "9:16", "4:3", "16:9"))},
                    {"callback_url", field()},
                    {"enable_prompt_expansion", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
                    {"reference_mask_urls", field()},
                    {"rendering_speed", field(enumValues("turbo", "balanced", "quality"))},
                    {"seed", field()},
                    {"source_image_url", field(required())},
                    {"strength", field()},
                    {"style", field(enumValues("auto", "general", "realistic", "design"))},
                    {"style_reference_image_urls", field()},
            })},
          })));
contract.put("ideogram-v3/text-to-image", new ContractAction(
    list("ideogram-v3-character", "ideogram-v3-text-to-image"),
          fieldsByModel(new Object[][] {
            {"ideogram-v3-character", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "3:4", "9:16", "4:3", "16:9"))},
                    {"callback_url", field()},
                    {"enable_prompt_expansion", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)))},
                    {"prompt", field()},
                    {"reference_image_urls", field(required())},
                    {"rendering_speed", field(enumValues("turbo", "balanced", "quality"))},
                    {"seed", field()},
                    {"style", field(enumValues("auto", "realistic", "fiction"))},
            })},
            {"ideogram-v3-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "3:4", "9:16", "4:3", "16:9"))},
                    {"callback_url", field()},
                    {"enable_prompt_expansion", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
                    {"rendering_speed", field(enumValues("turbo", "balanced", "quality"))},
                    {"seed", field()},
                    {"style", field(enumValues("auto", "general", "realistic", "design"))},
            })},
          })));
contract.put("imagen-4/remix-image", new ContractAction(
    list("imagen-4-pro-remix-image"),
          fieldsByModel(new Object[][] {
            {"imagen-4-pro-remix-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "2:3", "3:2", "3:4", "4:3", "4:5", "5:4", "9:16", "16:9", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_format", field(enumValues("png", "jpg"))},
                    {"output_resolution", field(enumValues("1k", "2k", "4k"))},
                    {"prompt", field()},
                    {"source_image_urls", field(required(), minItems(1), maxItems(8))},
            })},
          })));
contract.put("imagen-4/text-to-image", new ContractAction(
    list("imagen-4", "imagen-4-fast", "imagen-4-ultra"),
          fieldsByModel(new Object[][] {
            {"imagen-4", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "16:9", "9:16", "3:4", "4:3"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"prompt", field()},
                    {"seed", field()},
            })},
            {"imagen-4-fast", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "16:9", "9:16", "3:4", "4:3", "auto"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"prompt", field()},
                    {"seed", field()},
            })},
            {"imagen-4-ultra", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "16:9", "9:16", "3:4", "4:3"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"prompt", field()},
                    {"seed", field()},
            })},
          })));
contract.put("infinitetalk/audio-to-video", new ContractAction(
    list("infinitetalk-from-audio"),
          fieldsByModel(new Object[][] {
            {"infinitetalk-from-audio", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field()},
                    {"seed", field()},
                    {"source_audio_url", field(required())},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("kling/avatar", new ContractAction(
    list("kling-ai-avatar-pro", "kling-ai-avatar-standard", "kling-ai-avatar-v1-pro", "kling-v1-avatar-standard"),
          fieldsByModel(new Object[][] {
            {"kling-ai-avatar-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"prompt", field(required())},
                    {"source_audio_url", field(required())},
                    {"source_image_url", field(required())},
            })},
            {"kling-ai-avatar-standard", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"prompt", field(required())},
                    {"source_audio_url", field(required())},
                    {"source_image_url", field(required())},
            })},
            {"kling-ai-avatar-v1-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"prompt", field(required())},
                    {"source_audio_url", field(required())},
                    {"source_image_url", field(required())},
            })},
            {"kling-v1-avatar-standard", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"prompt", field(required())},
                    {"source_audio_url", field(required())},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("kling/extend-video", new ContractAction(
    list("kling-v2.5-turbo-image-to-video-pro", "kling-v2.5-turbo-text-to-video-pro"),
          fieldsByModel(new Object[][] {
            {"kling-v2.5-turbo-image-to-video-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"mode", field(enumValues("std", "pro"))},
                    {"prompt", field()},
                    {"source_task_id", field(required())},
            })},
            {"kling-v2.5-turbo-text-to-video-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"mode", field(enumValues("std", "pro"))},
                    {"prompt", field()},
                    {"source_task_id", field(required())},
            })},
          })));
  }

  private static void addActions5(Map<String, ContractAction> contract) {
contract.put("kling/image-to-video", new ContractAction(
    list("kling-v2.1-master-image-to-video", "kling-v2.1-pro", "kling-v2.1-standard", "kling-v2.5-turbo-image-to-video-pro", "kling-v2.6", "kling-v3-omni", "kling-v3-turbo-image-to-video"),
          fieldsByModel(new Object[][] {
            {"kling-v2.1-master-image-to-video", fields(new Object[][] {
                    {"aspect_ratio", field()},
                    {"callback_url", field()},
                    {"cfg_scale", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field(required())},
                    {"negative_prompt", field()},
                    {"prompt", field(required())},
            })},
            {"kling-v2.1-pro", fields(new Object[][] {
                    {"aspect_ratio", field()},
                    {"callback_url", field()},
                    {"cfg_scale", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field(required())},
                    {"negative_prompt", field()},
                    {"prompt", field(required())},
            })},
            {"kling-v2.1-standard", fields(new Object[][] {
                    {"aspect_ratio", field()},
                    {"callback_url", field()},
                    {"cfg_scale", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field(required())},
                    {"negative_prompt", field()},
                    {"prompt", field(required())},
            })},
            {"kling-v2.5-turbo-image-to-video-pro", fields(new Object[][] {
                    {"aspect_ratio", field()},
                    {"callback_url", field()},
                    {"cfg_scale", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field(required())},
                    {"negative_prompt", field()},
                    {"prompt", field(required())},
            })},
            {"kling-v2.6", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"enable_sound", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"mode", field(enumValues("std", "pro"))},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(2500.0)), length())},
            })},
            {"kling-v3-omni", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15)))},
                    {"enable_sound", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field(required())},
                    {"output_resolution", field(enumValues("720p", "1080p", "4k"))},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(2500.0)), length())},
            })},
            {"kling-v3-turbo-image-to-video", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15)))},
                    {"first_frame_image_url", field(required())},
                    {"model", field(required())},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(2500.0)), length())},
            })},
          }),
          rulesByModel(new Object[][] {
{"kling-v2.1-master-image-to-video", rules(rule(conditions(new Object[][] {{"model", "kling-v2.1-master-image-to-video"}}), list(), list("output_resolution", "enable_sound")))},
{"kling-v2.1-pro", rules(rule(conditions(new Object[][] {{"model", "kling-v2.1-pro"}}), list(), list("output_resolution", "enable_sound")))},
{"kling-v2.1-standard", rules(rule(conditions(new Object[][] {{"model", "kling-v2.1-standard"}}), list(), list("output_resolution", "enable_sound")))},
{"kling-v2.5-turbo-image-to-video-pro", rules(rule(conditions(new Object[][] {{"model", "kling-v2.5-turbo-image-to-video-pro"}}), list(), list("output_resolution", "enable_sound")))},
{"kling-v2.6", rules(rule(conditions(new Object[][] {{"model", "kling-v2.6"}}), list(), list("output_resolution", "negative_prompt", "cfg_scale")))},
{"kling-v3-omni", rules(rule(conditions(new Object[][] {{"model", "kling-v3-omni"}}), list(), list("negative_prompt", "cfg_scale")))},
{"kling-v3-turbo-image-to-video", rules(rule(conditions(new Object[][] {{"model", "kling-v3-turbo-image-to-video"}}), list(), list("enable_sound", "aspect_ratio", "negative_prompt", "cfg_scale", "last_frame_image_url")))},
          })));
contract.put("kling/motion-control", new ContractAction(
    list("kling-3.0", "kling-v2.6"),
          fieldsByModel(new Object[][] {
            {"kling-3.0", fields(new Object[][] {
                    {"background_source", field(enumValues("video", "image"))},
                    {"callback_url", field()},
                    {"character_orientation", field(enumValues("video", "image"))},
                    {"model", field(required())},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"reference_video_url", field(required())},
                    {"source_image_url", field(required())},
            })},
            {"kling-v2.6", fields(new Object[][] {
                    {"callback_url", field()},
                    {"character_orientation", field(required(), enumValues("video", "image"))},
                    {"model", field(required())},
                    {"output_resolution", field(required(), enumValues("720p", "1080p"))},
                    {"prompt", field(max(Double.valueOf(2500.0)), length())},
                    {"reference_video_url", field(required())},
                    {"source_image_url", field(required())},
            })},
          }),
          rulesByModel(new Object[][] {
{"kling-v2.6", rules(rule(conditions(new Object[][] {{"model", "kling-v2.6"}}), list(), list("background_source")))},
          })));
contract.put("kling/text-to-video", new ContractAction(
    list("kling-3.0", "kling-v2.1-master-text-to-video", "kling-v2.5-turbo-text-to-video-pro", "kling-v2.6", "kling-v3-omni", "kling-v3-turbo-text-to-video"),
          fieldsByModel(new Object[][] {
            {"kling-3.0", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1"))},
                    {"callback_url", field()},
                    {"cfg_scale", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15)))},
                    {"enable_sound", field()},
                    {"first_frame_image_url", field()},
                    {"kling_elements", field()},
                    {"last_frame_image_url", field()},
                    {"model", field(required())},
                    {"multi_prompt", field()},
                    {"multi_shots", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p", "4k"))},
                    {"prompt", field()},
            })},
            {"kling-v2.1-master-text-to-video", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1"))},
                    {"callback_url", field()},
                    {"cfg_scale", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"enable_sound", field()},
                    {"model", field(required())},
                    {"negative_prompt", field()},
                    {"prompt", field()},
            })},
            {"kling-v2.5-turbo-text-to-video-pro", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1"))},
                    {"callback_url", field()},
                    {"cfg_scale", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"enable_sound", field()},
                    {"model", field(required())},
                    {"negative_prompt", field()},
                    {"prompt", field()},
            })},
            {"kling-v2.6", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"enable_sound", field()},
                    {"mode", field(enumValues("std", "pro"))},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(2500.0)), length())},
            })},
            {"kling-v3-omni", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15)))},
                    {"enable_sound", field()},
                    {"model", field(required())},
                    {"output_resolution", field(enumValues("720p", "1080p", "4k"))},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(2500.0)), length())},
            })},
            {"kling-v3-turbo-text-to-video", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15)))},
                    {"model", field(required())},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(2500.0)), length())},
            })},
          }),
          rulesByModel(new Object[][] {
{"kling-v2.5-turbo-text-to-video-pro", rules(rule(conditions(new Object[][] {{"model", "kling-v2.5-turbo-text-to-video-pro"}}), list(), list("mode")))},
{"kling-v2.6", rules(rule(conditions(new Object[][] {{"model", "kling-v2.6"}}), list(), list("output_resolution", "negative_prompt", "cfg_scale", "multi_shots", "multi_prompt", "first_frame_image_url", "last_frame_image_url", "kling_elements")))},
{"kling-v3-omni", rules(rule(conditions(new Object[][] {{"model", "kling-v3-omni"}}), list(), list("negative_prompt", "cfg_scale", "multi_shots", "multi_prompt", "first_frame_image_url", "last_frame_image_url", "kling_elements")))},
{"kling-v3-turbo-text-to-video", rules(rule(conditions(new Object[][] {{"model", "kling-v3-turbo-text-to-video"}}), list(), list("enable_sound", "negative_prompt", "cfg_scale", "multi_shots", "multi_prompt", "first_frame_image_url", "last_frame_image_url", "kling_elements")))},
          })));
contract.put("luma/modify-video", new ContractAction(
    list("luma-modify-video"),
          fieldsByModel(new Object[][] {
            {"luma-modify-video", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field()},
                    {"prompt", field(required())},
                    {"source_video_url", field(required())},
                    {"watermark", field()},
            })},
          })));
contract.put("midjourney/edit-image", new ContractAction(
    list("midjourney-edit-image"),
          fieldsByModel(new Object[][] {
            {"midjourney-edit-image", fields(new Object[][] {
                    {"callback_url", field()},
                    {"include_split_images", field()},
                    {"mask_url", field()},
                    {"model", field()},
                    {"prompt", field(required())},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("midjourney/extend-video", new ContractAction(
    list("midjourney-image-to-video"),
          fieldsByModel(new Object[][] {
            {"midjourney-image-to-video", fields(new Object[][] {
                    {"callback_url", field()},
                    {"prompt", field()},
                    {"source_task_id", field(required())},
            })},
          })));
contract.put("midjourney/get-seed", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"image_id", field(required())},
            })},
          })));
contract.put("midjourney/image-to-prompt", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"source_image_url", field(required())},
            })},
          })));
  }

  private static void addActions6(Map<String, ContractAction> contract) {
contract.put("midjourney/image-to-video", new ContractAction(
    list("midjourney-image-to-video"),
          fieldsByModel(new Object[][] {
            {"midjourney-image-to-video", fields(new Object[][] {
                    {"callback_url", field()},
                    {"enable_loop", field()},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p"))},
                    {"prompt", field()},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("midjourney/shorten-prompt", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"prompt", field(required())},
            })},
          })));
contract.put("midjourney/text-to-image", new ContractAction(
    list("midjourney-v8.1"),
          fieldsByModel(new Object[][] {
            {"midjourney-v8.1", fields(new Object[][] {
                    {"callback_url", field()},
                    {"enable_prompt_translation", field()},
                    {"include_split_images", field()},
                    {"model", field()},
                    {"prompt", field(required())},
            })},
          })));
contract.put("nano-banana/edit-image", new ContractAction(
    list("nano-banana-2-lite", "nano-banana-edit"),
          fieldsByModel(new Object[][] {
            {"nano-banana-2-lite", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "1:4", "1:8", "2:3", "3:2", "3:4", "4:1", "4:3", "4:5", "5:4", "8:1", "9:16", "16:9", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(20000.0)), length())},
                    {"source_image_urls", field(required(), minItems(1), maxItems(10))},
            })},
            {"nano-banana-edit", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "9:16", "16:9", "3:4", "4:3", "3:2", "2:3", "5:4", "4:5", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_format", field(enumValues("png", "jpeg"))},
                    {"prompt", field()},
                    {"source_image_urls", field(required())},
            })},
          }),
          rulesByModel(new Object[][] {
{"nano-banana-2-lite", rules(rule(conditions(new Object[][] {{"model", "nano-banana-2-lite"}}), list(), list("output_format")))},
          })));
contract.put("nano-banana/text-to-image", new ContractAction(
    list("nano-banana", "nano-banana-2", "nano-banana-2-lite", "nano-banana-pro"),
          fieldsByModel(new Object[][] {
            {"nano-banana", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "9:16", "16:9", "3:4", "4:3", "3:2", "2:3", "5:4", "4:5", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_format", field(enumValues("png", "jpeg", "jpg"))},
                    {"output_resolution", field()},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
            })},
            {"nano-banana-2", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "1:4", "1:8", "2:3", "3:2", "3:4", "4:1", "4:3", "4:5", "5:4", "8:1", "9:16", "16:9", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_format", field(enumValues("png", "jpeg", "jpg"))},
                    {"output_resolution", field(enumValues("1k", "2k", "4k"))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
            })},
            {"nano-banana-2-lite", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "1:4", "1:8", "2:3", "3:2", "3:4", "4:1", "4:3", "4:5", "5:4", "8:1", "9:16", "16:9", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(20000.0)), length())},
                    {"reference_image_urls", field(maxItems(10))},
            })},
            {"nano-banana-pro", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "2:3", "3:2", "3:4", "4:3", "4:5", "5:4", "9:16", "16:9", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_format", field(enumValues("png", "jpeg", "jpg"))},
                    {"output_resolution", field(enumValues("1k", "2k", "4k"))},
                    {"prompt", field()},
                    {"reference_image_urls", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"nano-banana-2-lite", rules(rule(conditions(new Object[][] {{"model", "nano-banana-2-lite"}}), list(), list("output_resolution", "output_format")))},
          })));
contract.put("omnihuman/audio-to-video", new ContractAction(
    list("omnihuman-1.5"),
          fieldsByModel(new Object[][] {
            {"omnihuman-1.5", fields(new Object[][] {
                    {"callback_url", field()},
                    {"enable_fast_mode", field()},
                    {"mask_urls", field(maxItems(5))},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field(max(Double.valueOf(1000.0)), length())},
                    {"seed", field()},
                    {"source_audio_url", field(required())},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("omnihuman/human-identification", new ContractAction(
    list("omnihuman-1.5-human-identification"),
          fieldsByModel(new Object[][] {
            {"omnihuman-1.5-human-identification", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field()},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("omnihuman/subject-detection", new ContractAction(
    list("omnihuman-1.5-subject-detection"),
          fieldsByModel(new Object[][] {
            {"omnihuman-1.5-subject-detection", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field()},
                    {"source_image_url", field(required())},
            })},
          })));
  }

  private static void addActions7(Map<String, ContractAction> contract) {
contract.put("openai-tts/text-to-speech", new ContractAction(
    list("tts-1", "tts-1-hd"),
          fieldsByModel(new Object[][] {
            {"tts-1", fields(new Object[][] {
                    {"model", field()},
                    {"text", field(required(), max(Double.valueOf(4096.0)), length())},
            })},
            {"tts-1-hd", fields(new Object[][] {
                    {"model", field()},
                    {"text", field(required(), max(Double.valueOf(4096.0)), length())},
            })},
          })));
contract.put("producer/text-to-music", new ContractAction(
    list("fuzz-0.8", "fuzz-1.0", "fuzz-1.0-pro", "fuzz-1.1", "fuzz-1.1-pro", "fuzz-2.0", "fuzz-2.0-pro", "fuzz-2.0-raw"),
          fieldsByModel(new Object[][] {
            {"fuzz-0.8", fields(new Object[][] {
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(200.0)), length())},
                    {"title", field()},
                    {"vocal_mode", field(required(), enumValues("exact_lyrics", "instrumental"))},
            })},
            {"fuzz-1.0", fields(new Object[][] {
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(200.0)), length())},
                    {"title", field()},
                    {"vocal_mode", field(required(), enumValues("exact_lyrics", "instrumental"))},
            })},
            {"fuzz-1.0-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(200.0)), length())},
                    {"title", field()},
                    {"vocal_mode", field(required(), enumValues("exact_lyrics", "instrumental"))},
            })},
            {"fuzz-1.1", fields(new Object[][] {
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(200.0)), length())},
                    {"title", field()},
                    {"vocal_mode", field(required(), enumValues("exact_lyrics", "instrumental"))},
            })},
            {"fuzz-1.1-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(200.0)), length())},
                    {"title", field()},
                    {"vocal_mode", field(required(), enumValues("exact_lyrics", "instrumental"))},
            })},
            {"fuzz-2.0", fields(new Object[][] {
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(200.0)), length())},
                    {"title", field()},
                    {"vocal_mode", field(required(), enumValues("exact_lyrics", "instrumental"))},
            })},
            {"fuzz-2.0-pro", fields(new Object[][] {
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(200.0)), length())},
                    {"title", field()},
                    {"vocal_mode", field(required(), enumValues("exact_lyrics", "instrumental"))},
            })},
            {"fuzz-2.0-raw", fields(new Object[][] {
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(200.0)), length())},
                    {"title", field()},
                    {"vocal_mode", field(required(), enumValues("exact_lyrics", "instrumental"))},
            })},
          }),
          rulesByModel(new Object[][] {
{"fuzz-0.8", rules(rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics"), list()), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list(), list("lyrics")))},
{"fuzz-1.0", rules(rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics"), list()), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list(), list("lyrics")))},
{"fuzz-1.0-pro", rules(rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics"), list()), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list(), list("lyrics")))},
{"fuzz-1.1", rules(rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics"), list()), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list(), list("lyrics")))},
{"fuzz-1.1-pro", rules(rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics"), list()), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list(), list("lyrics")))},
{"fuzz-2.0", rules(rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics"), list()), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list(), list("lyrics")))},
{"fuzz-2.0-pro", rules(rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics"), list()), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list(), list("lyrics")))},
{"fuzz-2.0-raw", rules(rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics"), list()), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list(), list("lyrics")))},
          })));
contract.put("qwen-2/edit-image", new ContractAction(
    list("qwen-2-edit-image"),
          fieldsByModel(new Object[][] {
            {"qwen-2-edit-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "2:3", "3:2", "3:4", "4:3", "9:16", "16:9", "21:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_format", field(enumValues("jpeg", "png"))},
                    {"prompt", field(required())},
                    {"seed", field()},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("qwen-2/text-to-image", new ContractAction(
    list("qwen-2-text-to-image"),
          fieldsByModel(new Object[][] {
            {"qwen-2-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "3:4", "4:3", "9:16", "16:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_format", field(enumValues("png", "jpeg"))},
                    {"prompt", field(required())},
                    {"seed", field()},
            })},
          })));
contract.put("qwen-image/edit-image", new ContractAction(
    list("qwen-image-edit-image"),
          fieldsByModel(new Object[][] {
            {"qwen-image-edit-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "3:4", "9:16", "4:3", "16:9"))},
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"output_format", field(enumValues("png", "jpeg"))},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(2000.0)), length())},
                    {"seed", field()},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("qwen-image/remix-image", new ContractAction(
    list("qwen-image-remix-image"),
          fieldsByModel(new Object[][] {
            {"qwen-image-remix-image", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"output_format", field(enumValues("png", "jpeg"))},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(5000.0)), length())},
                    {"seed", field()},
                    {"source_image_url", field(required())},
                    {"strength", field(min(Double.valueOf(0.0)), max(Double.valueOf(1.0)))},
            })},
          })));
contract.put("qwen-image/text-to-image", new ContractAction(
    list("qwen-image-text-to-image"),
          fieldsByModel(new Object[][] {
            {"qwen-image-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "3:4", "9:16", "4:3", "16:9"))},
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"output_format", field(enumValues("png", "jpeg"))},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(5000.0)), length())},
                    {"seed", field()},
            })},
          })));
contract.put("recraft/remove-background", new ContractAction(
    list("recraft-remove-background"),
          fieldsByModel(new Object[][] {
            {"recraft-remove-background", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field()},
                    {"source_image_url", field(required())},
            })},
          })));
  }

  private static void addActions8(Map<String, ContractAction> contract) {
contract.put("recraft/upscale-image", new ContractAction(
    list("recraft-crisp-upscale"),
          fieldsByModel(new Object[][] {
            {"recraft-crisp-upscale", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field()},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("runway-aleph/edit-video", new ContractAction(
    list("runway-aleph"),
          fieldsByModel(new Object[][] {
            {"runway-aleph", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "4:3", "3:4", "1:1", "21:9"))},
                    {"callback_url", field()},
                    {"model", field()},
                    {"prompt", field(required())},
                    {"reference_image_url", field()},
                    {"seed", field()},
                    {"source_video_url", field(required())},
                    {"watermark", field()},
            })},
          })));
contract.put("runway/extend-video", new ContractAction(
    list("runway"),
          fieldsByModel(new Object[][] {
            {"runway", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field()},
                    {"output_resolution", field(required(), enumValues("720p", "1080p"))},
                    {"prompt", field(required())},
                    {"source_task_id", field(required())},
                    {"watermark", field()},
            })},
          })));
contract.put("runway/text-to-video", new ContractAction(
    list("runway"),
          fieldsByModel(new Object[][] {
            {"runway", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1", "4:3", "3:4"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(required(), enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"first_frame_image_url", field()},
                    {"model", field()},
                    {"output_resolution", field(required(), enumValues("720p", "1080p"))},
                    {"prompt", field(required())},
                    {"watermark", field()},
            })},
          })));
contract.put("seedance/text-to-video", new ContractAction(
    list("seedance-1.5-pro", "seedance-2-mini", "seedance-2.0", "seedance-2.0-fast", "seedance-v1-lite", "seedance-v1-pro", "seedance-v1-pro-fast"),
          fieldsByModel(new Object[][] {
            {"seedance-1.5-pro", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "21:9"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(required(), min(Double.valueOf(4.0)), max(Double.valueOf(12.0)))},
                    {"enable_safety_checker", field()},
                    {"generate_audio", field()},
                    {"lock_camera", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p", "720p", "1080p"))},
                    {"prompt", field()},
                    {"seed", field(min(Double.valueOf(-1.0)), max(Double.valueOf(2147483647.0)))},
                    {"source_image_urls", field(maxItems(2))},
            })},
            {"seedance-2-mini", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(min(Double.valueOf(4.0)), max(Double.valueOf(15.0)))},
                    {"first_frame_image_url", field()},
                    {"generate_audio", field()},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field()},
                    {"reference_audio_urls", field(maxItems(3))},
                    {"reference_image_urls", field(maxItems(9))},
                    {"reference_video_urls", field(maxItems(3))},
                    {"web_search", field()},
            })},
            {"seedance-2.0", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(min(Double.valueOf(4.0)), max(Double.valueOf(15.0)))},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"generate_audio", field()},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p", "720p", "1080p", "4k"))},
                    {"prompt", field()},
                    {"reference_audio_urls", field(maxItems(3))},
                    {"reference_image_urls", field(maxItems(9))},
                    {"reference_video_urls", field(maxItems(3))},
                    {"web_search", field()},
            })},
            {"seedance-2.0-fast", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "21:9", "auto"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(min(Double.valueOf(4.0)), max(Double.valueOf(15.0)))},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"generate_audio", field()},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field()},
                    {"reference_audio_urls", field(maxItems(3))},
                    {"reference_image_urls", field(maxItems(9))},
                    {"reference_video_urls", field(maxItems(3))},
                    {"web_search", field()},
            })},
            {"seedance-v1-lite", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "9:21"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(required(), enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"last_frame_image_url", field()},
                    {"lock_camera", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p", "720p", "1080p"))},
                    {"prompt", field()},
                    {"seed", field()},
            })},
            {"seedance-v1-pro", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "21:9"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(required(), enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"lock_camera", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p", "720p", "1080p"))},
                    {"prompt", field()},
                    {"seed", field()},
            })},
            {"seedance-v1-pro-fast", fields(new Object[][] {
                    {"callback_url", field()},
                    {"duration_seconds", field(required(), enumValues(Integer.valueOf(5), Integer.valueOf(10)))},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field(required())},
                    {"model", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"seed", field(min(Double.valueOf(-1.0)), max(Double.valueOf(2147483647.0)))},
            })},
          })));
contract.put("seedream/edit-image", new ContractAction(
    list("seedream-4.5-edit", "seedream-5-lite-edit", "seedream-5-pro-edit", "seedream-v4-edit"),
          fieldsByModel(new Object[][] {
            {"seedream-4.5-edit", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "2:3", "3:2", "21:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_count", field()},
                    {"output_format", field()},
                    {"output_quality", field(required(), enumValues("basic", "high"))},
                    {"output_resolution", field()},
                    {"prompt", field()},
                    {"seed", field()},
                    {"source_image_urls", field(required(), minItems(1), maxItems(14))},
            })},
            {"seedream-5-lite-edit", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "2:3", "3:2", "21:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_count", field()},
                    {"output_format", field(enumValues("png", "jpeg"))},
                    {"output_quality", field(required(), enumValues("basic", "high"))},
                    {"output_resolution", field()},
                    {"prompt", field()},
                    {"seed", field()},
                    {"source_image_urls", field(required(), minItems(1), maxItems(14))},
            })},
            {"seedream-5-pro-edit", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "2:3", "3:2", "21:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_format", field(enumValues("png", "jpeg"))},
                    {"output_quality", field(required(), enumValues("basic", "high"))},
                    {"prompt", field(required(), min(Double.valueOf(3.0)), max(Double.valueOf(5000.0)), length())},
                    {"source_image_urls", field(required(), minItems(1), maxItems(10))},
            })},
            {"seedream-v4-edit", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "3:2", "2:3", "16:9", "9:16", "21:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6)))},
                    {"output_format", field()},
                    {"output_quality", field()},
                    {"output_resolution", field(enumValues("1k", "2k", "4k"))},
                    {"prompt", field()},
                    {"seed", field()},
                    {"source_image_urls", field(required(), minItems(1), maxItems(10))},
            })},
          }),
          rulesByModel(new Object[][] {
{"seedream-4.5-edit", rules(rule(conditions(new Object[][] {{"model", "seedream-4.5-edit"}}), list(), list("output_format")))},
{"seedream-5-pro-edit", rules(rule(conditions(new Object[][] {{"model", "seedream-5-pro-edit"}}), list(), list("output_resolution", "output_count", "seed")))},
{"seedream-v4-edit", rules(rule(conditions(new Object[][] {{"model", "seedream-v4-edit"}}), list(), list("output_format")))},
          })));
contract.put("seedream/text-to-image", new ContractAction(
    list("seedream-4.5-text-to-image", "seedream-5-lite-text-to-image", "seedream-5-pro-text-to-image", "seedream-v4-text-to-image"),
          fieldsByModel(new Object[][] {
            {"seedream-4.5-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "2:3", "3:2", "21:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_count", field()},
                    {"output_format", field()},
                    {"output_quality", field(required(), enumValues("basic", "high"))},
                    {"output_resolution", field()},
                    {"prompt", field()},
                    {"seed", field()},
            })},
            {"seedream-5-lite-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "2:3", "3:2", "21:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_count", field()},
                    {"output_format", field(enumValues("png", "jpeg"))},
                    {"output_quality", field(required(), enumValues("basic", "high"))},
                    {"output_resolution", field()},
                    {"prompt", field()},
                    {"seed", field()},
            })},
            {"seedream-5-pro-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "4:3", "3:4", "16:9", "9:16", "2:3", "3:2", "21:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_format", field(enumValues("png", "jpeg"))},
                    {"output_quality", field(required(), enumValues("basic", "high"))},
                    {"prompt", field(required(), min(Double.valueOf(3.0)), max(Double.valueOf(5000.0)), length())},
            })},
            {"seedream-v4-text-to-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "4:3", "3:4", "3:2", "2:3", "16:9", "9:16", "21:9"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_count", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6)))},
                    {"output_format", field()},
                    {"output_quality", field()},
                    {"output_resolution", field(enumValues("1k", "2k", "4k"))},
                    {"prompt", field()},
                    {"seed", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"seedream-4.5-text-to-image", rules(rule(conditions(new Object[][] {{"model", "seedream-4.5-text-to-image"}}), list(), list("output_format")))},
{"seedream-5-pro-text-to-image", rules(rule(conditions(new Object[][] {{"model", "seedream-5-pro-text-to-image"}}), list(), list("output_resolution", "output_count", "seed")))},
{"seedream-v4-text-to-image", rules(rule(conditions(new Object[][] {{"model", "seedream-v4-text-to-image"}}), list(), list("output_format")))},
          })));
  }

  private static void addActions9(Map<String, ContractAction> contract) {
contract.put("suno/add-instrumental", new ContractAction(
    list("suno-v4.5-plus", "suno-v5", "suno-v5.5"),
          fieldsByModel(new Object[][] {
            {"suno-v4.5-plus", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"style_weight", field()},
                    {"tags", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"style_weight", field()},
                    {"tags", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5.5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"style_weight", field()},
                    {"tags", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
          })));
contract.put("suno/add-vocals", new ContractAction(
    list("suno-v4.5-plus", "suno-v5"),
          fieldsByModel(new Object[][] {
            {"suno-v4.5-plus", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field(required())},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field(required())},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
          })));
contract.put("suno/blend-lyrics", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"lyrics_a", field(required())},
                    {"lyrics_b", field(required())},
            })},
          })));
contract.put("suno/boost-style", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"description", field()},
            })},
          })));
contract.put("suno/check-voice", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"task_id", field(required())},
            })},
          })));
contract.put("suno/convert-audio", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"audio_id", field()},
                    {"callback_url", field()},
                    {"task_id", field()},
            })},
          })));
contract.put("suno/cover-audio", new ContractAction(
    list("suno-v4", "suno-v4.5", "suno-v4.5-all", "suno-v4.5-plus", "suno-v5", "suno-v5.5"),
          fieldsByModel(new Object[][] {
            {"suno-v4", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5-all", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5-plus", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5.5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url", field(required())},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"suno-v4", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v4.5", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v4.5-all", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v4.5-plus", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v5", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v5.5", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
          })));
  }

  private static void addActions10(Map<String, ContractAction> contract) {
contract.put("suno/create-mashup", new ContractAction(
    list("suno-v4", "suno-v4.5", "suno-v4.5-all", "suno-v4.5-plus", "suno-v5", "suno-v5.5"),
          fieldsByModel(new Object[][] {
            {"suno-v4", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url_list", field(required(), minItems(2), maxItems(2))},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url_list", field(required(), minItems(2), maxItems(2))},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5-all", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url_list", field(required(), minItems(2), maxItems(2))},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5-plus", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url_list", field(required(), minItems(2), maxItems(2))},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url_list", field(required(), minItems(2), maxItems(2))},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5.5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"upload_url_list", field(required(), minItems(2), maxItems(2))},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"suno-v4", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v4.5", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v4.5-all", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v4.5-plus", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v5", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v5.5", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
          })));
contract.put("suno/extend-music", new ContractAction(
    list("suno-v4", "suno-v4.5", "suno-v4.5-all", "suno-v4.5-plus", "suno-v5", "suno-v5.5"),
          fieldsByModel(new Object[][] {
            {"suno-v4", fields(new Object[][] {
                    {"audio_id", field()},
                    {"audio_url", field()},
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"instrumental", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"parameter_mode", field(enumValues("source", "custom"))},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"task_id", field()},
                    {"title", field()},
                    {"upload_url", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5", fields(new Object[][] {
                    {"audio_id", field()},
                    {"audio_url", field()},
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"instrumental", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"parameter_mode", field(enumValues("source", "custom"))},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"task_id", field()},
                    {"title", field()},
                    {"upload_url", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5-all", fields(new Object[][] {
                    {"audio_id", field()},
                    {"audio_url", field()},
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"instrumental", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"parameter_mode", field(enumValues("source", "custom"))},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"task_id", field()},
                    {"title", field()},
                    {"upload_url", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5-plus", fields(new Object[][] {
                    {"audio_id", field()},
                    {"audio_url", field()},
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"instrumental", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"parameter_mode", field(enumValues("source", "custom"))},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"task_id", field()},
                    {"title", field()},
                    {"upload_url", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5", fields(new Object[][] {
                    {"audio_id", field()},
                    {"audio_url", field()},
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"instrumental", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"parameter_mode", field(enumValues("source", "custom"))},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"task_id", field()},
                    {"title", field()},
                    {"upload_url", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5.5", fields(new Object[][] {
                    {"audio_id", field()},
                    {"audio_url", field()},
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"instrumental", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"parameter_mode", field(enumValues("source", "custom"))},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"task_id", field()},
                    {"title", field()},
                    {"upload_url", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"weirdness_constraint", field()},
            })},
          })));
contract.put("suno/generate-artwork", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"task_id", field(required())},
            })},
          })));
contract.put("suno/generate-lyrics", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"prompt", field(required())},
            })},
          })));
contract.put("suno/generate-midi", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"task_id", field()},
            })},
          })));
contract.put("suno/generate-persona", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"audio_id", field()},
                    {"description", field()},
                    {"name", field()},
                    {"task_id", field()},
            })},
          })));
  }

  private static void addActions11(Map<String, ContractAction> contract) {
contract.put("suno/generate-voice", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"description", field()},
                    {"singer_skill_level", field(enumValues("beginner", "intermediate", "advanced", "professional"))},
                    {"style", field()},
                    {"task_id", field(required())},
                    {"verify_url", field(required())},
                    {"voice_name", field()},
            })},
          })));
contract.put("suno/get-timestamped-lyrics", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"audio_id", field()},
                    {"task_id", field()},
            })},
          })));
contract.put("suno/regenerate-validation-phrase", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"task_id", field(required())},
            })},
          })));
contract.put("suno/replace-section", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"audio_id", field()},
                    {"callback_url", field()},
                    {"full_lyrics", field(required())},
                    {"infill_end_time", field(required())},
                    {"infill_start_time", field(required())},
                    {"lyrics", field(required())},
                    {"model", field(enumValues("suno-v4", "suno-v4.5", "suno-v4.5-all", "suno-v4.5-plus", "suno-v5", "suno-v5.5"))},
                    {"negative_tags", field()},
                    {"tags", field(required())},
                    {"task_id", field()},
                    {"title", field(required())},
                    {"upload_url", field()},
            })},
          })));
contract.put("suno/separate-audio-stems", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"audio_id", field(required())},
                    {"callback_url", field()},
                    {"stem_name", field(enumValues("Lead Vocal", "Drum Kit", "Kick", "Snare", "Risers", "Bass", "Backing Vocals", "Piano", "Electric Guitar", "Percussion", "String Section", "Synth", "Acoustic Guitar", "Sound Effects", "Synth Pad", "Synth Bass", "Guitar", "Brass Section", "Organ", "Electronic Drum Kit", "Lead Electric Guitar", "Synth Keys", "Rhythm Electric Guitar", "Electric Piano", "Upright Bass", "Keyboards", "Distorted Electric Guitar", "Synth Strings", "Synth Lead", "Woodwinds", "Rhythm Acoustic Guitar", "Flute", "Harp", "Tambourine", "Trumpet", "Arpeggiator", "Accordion", "Fiddle", "Pedal Steel Guitar", "Synth Voice", "Violin", "Digital Piano", "Synth Brass", "Mandolin", "Choir", "Banjo", "Bells", "Clarinet", "Tenor Saxophone", "Trombone", "Shaker", "French Horn", "Glockenspiel", "Electric Bass", "Cello", "Timpani", "Harmonica", "Marimba", "Vibraphone", "Lap Steel Guitar", "Saxophone", "Orchestra", "Horns", "Cymbals", "Hand Clap", "Oboe", "Celesta", "Congas", "Drone", "Alto Saxophone", "Double Bass", "Ukulele", "Harpsichord", "Baritone Saxophone", "Xylophone", "Tuba", "Bass Guitar", "Whistle", "Lead Guitar", "Rhodes", "808", "Bongos", "Bassoon", "Cowbell", "Viola", "Sitar", "Steel Drums", "Piccolo", "Theremin", "Bagpipes", "Hi-Hat", "Music Box", "Melodica", "Tabla", "Koto", "Djembe", "Taiko", "Didgeridoo"))},
                    {"task_id", field(required())},
                    {"type", field(enumValues("separate_vocal", "split_stem", "split_stem_advanced"))},
            })},
          }),
          rulesByModel(new Object[][] {
{"_", rules(rule(conditions(new Object[][] {{"type", "split_stem_advanced"}}), list("stem_name"), list()))},
          })));
contract.put("suno/text-to-music", new ContractAction(
    list("suno-v4", "suno-v4.5", "suno-v4.5-all", "suno-v4.5-plus", "suno-v5", "suno-v5.5"),
          fieldsByModel(new Object[][] {
            {"suno-v4", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"duration_seconds", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"duration_seconds", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5-all", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"duration_seconds", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v4.5-plus", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"duration_seconds", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"duration_seconds", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
            {"suno-v5.5", fields(new Object[][] {
                    {"audio_weight", field()},
                    {"callback_url", field()},
                    {"continue_at", field()},
                    {"duration_seconds", field()},
                    {"lyrics", field()},
                    {"model", field(required())},
                    {"negative_tags", field()},
                    {"persona_id", field()},
                    {"persona_type", field(enumValues("style", "voice"))},
                    {"prompt", field()},
                    {"style", field()},
                    {"style_weight", field()},
                    {"title", field()},
                    {"vocal_gender", field(enumValues("male", "female"))},
                    {"vocal_mode", field(required(), enumValues("auto_lyrics", "exact_lyrics", "instrumental"))},
                    {"weirdness_constraint", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"suno-v4", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v4.5", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v4.5-all", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v4.5-plus", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v5", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
{"suno-v5.5", rules(rule(conditions(new Object[][] {{"vocal_mode", "auto_lyrics"}}), list("prompt"), list("lyrics", "style", "title")), rule(conditions(new Object[][] {{"vocal_mode", "exact_lyrics"}}), list("lyrics", "style", "title"), list("prompt")), rule(conditions(new Object[][] {{"vocal_mode", "instrumental"}}), list("style", "title"), list("prompt", "lyrics")))},
          })));
contract.put("suno/text-to-sound", new ContractAction(
    list("suno-v5", "suno-v5.5"),
          fieldsByModel(new Object[][] {
            {"suno-v5", fields(new Object[][] {
                    {"callback_url", field()},
                    {"grab_lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required())},
                    {"sound_key", field(enumValues("Cm", "C#m", "Dm", "D#m", "Em", "Fm", "F#m", "Gm", "G#m", "Am", "A#m", "Bm", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"))},
                    {"sound_loop", field()},
                    {"sound_tempo", field()},
            })},
            {"suno-v5.5", fields(new Object[][] {
                    {"callback_url", field()},
                    {"grab_lyrics", field()},
                    {"model", field(required())},
                    {"prompt", field(required())},
                    {"sound_key", field(enumValues("Cm", "C#m", "Dm", "D#m", "Em", "Fm", "F#m", "Gm", "G#m", "Am", "A#m", "Bm", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"))},
                    {"sound_loop", field()},
                    {"sound_tempo", field()},
            })},
          })));
contract.put("suno/visualize-music", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"audio_id", field()},
                    {"author", field()},
                    {"callback_url", field()},
                    {"domain_name", field()},
                    {"task_id", field()},
            })},
          })));
  }

  private static void addActions12(Map<String, ContractAction> contract) {
contract.put("suno/voice-to-validation-phrase", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"language", field(enumValues("en", "zh", "es", "fr", "pt", "de", "ja", "ko", "hi", "ru"))},
                    {"vocal_end_seconds", field(required())},
                    {"vocal_start_seconds", field(required())},
                    {"voice_url", field()},
            })},
          })));
contract.put("topaz/upscale-image", new ContractAction(
    list("topaz-upscale-image"),
          fieldsByModel(new Object[][] {
            {"topaz-upscale-image", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field()},
                    {"source_image_url", field(required())},
                    {"upscale_factor", field(required(), enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(4), Integer.valueOf(8)))},
            })},
          })));
contract.put("topaz/upscale-video", new ContractAction(
    list("topaz-upscale-video"),
          fieldsByModel(new Object[][] {
            {"topaz-upscale-video", fields(new Object[][] {
                    {"callback_url", field()},
                    {"model", field()},
                    {"source_video_url", field(required())},
                    {"upscale_factor", field(enumValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(4)))},
            })},
          })));
contract.put("veo-3-1/extend-video", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"prompt", field()},
                    {"seeds", field()},
                    {"source_task_id", field(required())},
                    {"watermark", field()},
            })},
          })));
contract.put("veo-3-1/text-to-video", new ContractAction(
    list("veo-3.1", "veo-3.1-fast", "veo-3.1-lite"),
          fieldsByModel(new Object[][] {
            {"veo-3.1", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "auto"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(8)))},
                    {"enable_translation", field()},
                    {"first_frame_image_url", field()},
                    {"input_mode", field(enumValues("text", "first_and_last_frames", "reference"))},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"prompt", field()},
                    {"reference_image_urls", field(minItems(1), maxItems(3))},
                    {"seeds", field()},
                    {"watermark", field()},
            })},
            {"veo-3.1-fast", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "auto"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(8)))},
                    {"enable_translation", field()},
                    {"first_frame_image_url", field()},
                    {"input_mode", field(enumValues("text", "first_and_last_frames", "reference"))},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"prompt", field()},
                    {"reference_image_urls", field(minItems(1), maxItems(3))},
                    {"seeds", field()},
                    {"watermark", field()},
            })},
            {"veo-3.1-lite", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16"))},
                    {"callback_url", field()},
                    {"duration_seconds", field(enumValues(Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(8)))},
                    {"enable_translation", field()},
                    {"first_frame_image_url", field()},
                    {"input_mode", field(enumValues("text", "first_and_last_frames", "reference"))},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"prompt", field()},
                    {"reference_image_urls", field(minItems(1), maxItems(3))},
                    {"seeds", field()},
                    {"watermark", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"veo-3.1-lite", rules(rule(conditions(new Object[][] {{"model", "veo-3.1-lite"}}), list(), list("seeds", "output_resolution")), rule(conditions(new Object[][] {{"duration_seconds", Integer.valueOf(4)}, {"input_mode", "reference"}, {"model", "veo-3.1-lite"}}), list(), list("duration_seconds")), rule(conditions(new Object[][] {{"duration_seconds", Integer.valueOf(6)}, {"input_mode", "reference"}, {"model", "veo-3.1-lite"}}), list(), list("duration_seconds")))},
          })));
contract.put("veo-3-1/upscale-video", new ContractAction(
    list(),
          fieldsByModel(new Object[][] {
            {"_", fields(new Object[][] {
                    {"callback_url", field()},
                    {"index", field()},
                    {"output_resolution", field(enumValues("1080p", "4k"))},
                    {"source_task_id", field()},
            })},
          })));
contract.put("volcengine-lip-sync/lip-sync-video", new ContractAction(
    list("volcengine-lip-sync"),
          fieldsByModel(new Object[][] {
            {"volcengine-lip-sync", fields(new Object[][] {
                    {"align_audio", field()},
                    {"align_audio_reverse", field()},
                    {"callback_url", field()},
                    {"enable_scene_detection", field()},
                    {"enable_vocal_separation", field()},
                    {"mode", field(required(), enumValues("lite", "basic"))},
                    {"model", field()},
                    {"source_audio_url", field(required())},
                    {"source_video_url", field(required())},
                    {"template_start_seconds", field(min(Double.valueOf(0.0)))},
            })},
          }),
          rulesByModel(new Object[][] {
{"volcengine-lip-sync", rules(rule(conditions(new Object[][] {{"mode", "lite"}}), list(), list("enable_scene_detection")), rule(conditions(new Object[][] {{"mode", "basic"}}), list(), list("align_audio", "align_audio_reverse", "template_start_seconds")), rule(conditions(new Object[][] {{"align_audio_reverse", true}}), list("align_audio"), list()))},
          })));
contract.put("wan/animate", new ContractAction(
    list("wan-2.2-animate-move", "wan-2.2-animate-replace"),
          fieldsByModel(new Object[][] {
            {"wan-2.2-animate-move", fields(new Object[][] {
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p", "580p", "720p"))},
                    {"reference_video_url", field(required())},
                    {"source_image_url", field(required())},
            })},
            {"wan-2.2-animate-replace", fields(new Object[][] {
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"output_resolution", field(enumValues("480p", "580p", "720p"))},
                    {"reference_video_url", field(required())},
                    {"source_image_url", field(required())},
            })},
          })));
  }

  private static void addActions13(Map<String, ContractAction> contract) {
contract.put("wan/edit-video", new ContractAction(
    list("wan-2.6-edit-video", "wan-2.6-flash-edit-video", "wan-2.7-edit-video"),
          fieldsByModel(new Object[][] {
            {"wan-2.6-edit-video", fields(new Object[][] {
                    {"aspect_ratio", field()},
                    {"audio", field()},
                    {"audio_setting", field()},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"multi_shots", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field(required())},
                    {"reference_image_url", field()},
                    {"seed", field()},
                    {"source_video_url", field()},
                    {"source_video_urls", field(required())},
                    {"watermark", field()},
            })},
            {"wan-2.6-flash-edit-video", fields(new Object[][] {
                    {"aspect_ratio", field()},
                    {"audio", field()},
                    {"audio_setting", field()},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"multi_shots", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field()},
                    {"prompt", field(required())},
                    {"reference_image_url", field()},
                    {"seed", field()},
                    {"source_video_url", field()},
                    {"source_video_urls", field(required())},
                    {"watermark", field()},
            })},
            {"wan-2.7-edit-video", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1", "4:3", "3:4"))},
                    {"audio", field()},
                    {"audio_setting", field()},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"multi_shots", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"reference_image_url", field()},
                    {"seed", field()},
                    {"source_video_url", field(required())},
                    {"source_video_urls", field()},
                    {"watermark", field()},
            })},
          })));
contract.put("wan/image-to-video", new ContractAction(
    list("wan-2.2-a14b-image-to-video-turbo", "wan-2.5-image-to-video", "wan-2.6-flash-image-to-video", "wan-2.6-image-to-video", "wan-2.7-image-to-video"),
          fieldsByModel(new Object[][] {
            {"wan-2.2-a14b-image-to-video-turbo", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field()},
                    {"audio", field()},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"driving_audio_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"multi_shots", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("480p", "720p"))},
                    {"prompt", field()},
                    {"ratio", field()},
                    {"seed", field()},
                    {"source_video_url", field()},
                    {"watermark", field()},
            })},
            {"wan-2.5-image-to-video", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field()},
                    {"audio", field()},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"driving_audio_url", field()},
                    {"duration_seconds", field(required())},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"multi_shots", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"ratio", field()},
                    {"seed", field()},
                    {"source_video_url", field()},
                    {"watermark", field()},
            })},
            {"wan-2.6-flash-image-to-video", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field()},
                    {"audio", field(required())},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"driving_audio_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"multi_shots", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field(required())},
                    {"ratio", field()},
                    {"source_video_url", field()},
                    {"watermark", field()},
            })},
            {"wan-2.6-image-to-video", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field()},
                    {"audio", field()},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"driving_audio_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field(required())},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"multi_shots", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field(required())},
                    {"ratio", field()},
                    {"source_video_url", field()},
                    {"watermark", field()},
            })},
            {"wan-2.7-image-to-video", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field()},
                    {"audio", field()},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"driving_audio_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"last_frame_image_url", field()},
                    {"model", field()},
                    {"multi_shots", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field(required())},
                    {"ratio", field()},
                    {"seed", field()},
                    {"source_video_url", field()},
                    {"watermark", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"wan-2.6-flash-image-to-video", rules(rule(conditions(new Object[][] {{"model", "wan-2.6-flash-image-to-video"}}), list(), list("seed")))},
{"wan-2.6-image-to-video", rules(rule(conditions(new Object[][] {{"model", "wan-2.6-image-to-video"}}), list(), list("seed")))},
          })));
contract.put("wan/speech-to-video", new ContractAction(
    list("wan-2.2-a14b-speech-to-video-turbo"),
          fieldsByModel(new Object[][] {
            {"wan-2.2-a14b-speech-to-video-turbo", fields(new Object[][] {
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"frames_per_second", field()},
                    {"guidance_scale", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"num_frames", field()},
                    {"num_inference_steps", field()},
                    {"output_resolution", field(enumValues("480p", "580p", "720p"))},
                    {"prompt", field(required())},
                    {"seed", field()},
                    {"shift", field()},
                    {"source_audio_url", field(required())},
                    {"source_image_url", field(required())},
            })},
          })));
contract.put("wan/text-to-image", new ContractAction(
    list("wan-2.7-image", "wan-2.7-image-pro"),
          fieldsByModel(new Object[][] {
            {"wan-2.7-image", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "16:9", "4:3", "21:9", "3:4", "9:16", "8:1", "1:8"))},
                    {"bbox_list", field()},
                    {"callback_url", field()},
                    {"color_palette", field()},
                    {"enable_safety_checker", field()},
                    {"enable_sequential", field()},
                    {"model", field()},
                    {"output_count", field()},
                    {"output_resolution", field(enumValues("1k", "2k", "4k"))},
                    {"prompt", field()},
                    {"seed", field()},
                    {"source_image_urls", field()},
                    {"thinking_mode", field()},
                    {"watermark", field()},
            })},
            {"wan-2.7-image-pro", fields(new Object[][] {
                    {"aspect_ratio", field(enumValues("1:1", "16:9", "4:3", "21:9", "3:4", "9:16", "8:1", "1:8"))},
                    {"bbox_list", field()},
                    {"callback_url", field()},
                    {"color_palette", field()},
                    {"enable_safety_checker", field()},
                    {"enable_sequential", field()},
                    {"model", field()},
                    {"output_count", field()},
                    {"output_resolution", field(enumValues("1k", "2k", "4k"))},
                    {"prompt", field()},
                    {"seed", field()},
                    {"source_image_urls", field()},
                    {"thinking_mode", field()},
                    {"watermark", field()},
            })},
          })));
  }

  private static void addActions14(Map<String, ContractAction> contract) {
contract.put("wan/text-to-video", new ContractAction(
    list("wan-2.2-a14b-text-to-video-turbo", "wan-2.5-text-to-video", "wan-2.6-text-to-video", "wan-2.7-r2v", "wan-2.7-text-to-video"),
          fieldsByModel(new Object[][] {
            {"wan-2.2-a14b-text-to-video-turbo", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field()},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("480p", "580p", "720p"))},
                    {"prompt", field()},
                    {"ratio", field()},
                    {"reference_audio_url", field()},
                    {"reference_image_urls", field()},
                    {"reference_video_urls", field()},
                    {"seed", field()},
                    {"watermark", field()},
            })},
            {"wan-2.5-text-to-video", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field()},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"ratio", field()},
                    {"reference_audio_url", field()},
                    {"reference_image_urls", field()},
                    {"reference_video_urls", field()},
                    {"seed", field()},
                    {"watermark", field()},
            })},
            {"wan-2.6-text-to-video", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field()},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"ratio", field()},
                    {"reference_audio_url", field()},
                    {"reference_image_urls", field()},
                    {"reference_video_urls", field()},
                    {"watermark", field()},
            })},
            {"wan-2.7-r2v", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field(enumValues("16:9", "9:16", "1:1", "4:3", "3:4"))},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"duration_seconds", field(min(Double.valueOf(2.0)), max(Double.valueOf(10.0)))},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"ratio", field()},
                    {"reference_audio_url", field()},
                    {"reference_image_urls", field()},
                    {"reference_video_urls", field()},
                    {"seed", field()},
                    {"watermark", field()},
            })},
            {"wan-2.7-text-to-video", fields(new Object[][] {
                    {"acceleration", field()},
                    {"aspect_ratio", field()},
                    {"background_audio_url", field()},
                    {"callback_url", field()},
                    {"duration_seconds", field()},
                    {"enable_prompt_expansion", field()},
                    {"enable_safety_checker", field()},
                    {"first_frame_image_url", field()},
                    {"model", field()},
                    {"negative_prompt", field()},
                    {"output_resolution", field(enumValues("720p", "1080p"))},
                    {"prompt", field()},
                    {"ratio", field()},
                    {"reference_audio_url", field()},
                    {"reference_image_urls", field()},
                    {"reference_video_urls", field()},
                    {"seed", field()},
                    {"watermark", field()},
            })},
          }),
          rulesByModel(new Object[][] {
{"wan-2.6-text-to-video", rules(rule(conditions(new Object[][] {{"model", "wan-2.6-text-to-video"}}), list(), list("seed")))},
          })));
contract.put("z-image/text-to-image", new ContractAction(
    list("z-image"),
          fieldsByModel(new Object[][] {
            {"z-image", fields(new Object[][] {
                    {"aspect_ratio", field(required(), enumValues("1:1", "4:3", "3:4", "16:9", "9:16"))},
                    {"callback_url", field()},
                    {"enable_safety_checker", field()},
                    {"model", field()},
                    {"prompt", field(required(), min(Double.valueOf(1.0)), max(Double.valueOf(1000.0)), length())},
            })},
          })));
  }
}
