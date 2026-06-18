import type { AsyncTaskStatus } from '@runapi.ai/core';

/** Image upscaling model slug. */
export type TopazUpscaleImageModel = 'topaz-upscale-image';
/** Video upscaling model slug. */
export type TopazUpscaleVideoModel = 'topaz-upscale-video';
/** Image upscale factor. Higher factors produce larger output but take longer. */
export type UpscaleImageFactor = 1 | 2 | 4 | 8;
/** Video upscale factor. Max 4x (8x is not supported for video). */
export type UpscaleVideoFactor = 1 | 2 | 4;

/**
 * Parameters for image upscaling. All fields except `callback_url` are required.
 * Output dimensions equal source dimensions multiplied by `upscale_factor`.
 */
export interface UpscaleImageParams {
  model: TopazUpscaleImageModel;
  /** Public URL of the source image to upscale. */
  source_image_url: string;
  /** Multiplier for the output resolution (1x, 2x, 4x, or 8x). */
  upscale_factor: UpscaleImageFactor;
  /** HTTPS callback URL for task completion notification. */
  callback_url?: string;
}

/**
 * Parameters for video upscaling. `upscale_factor` is optional and defaults to 2x.
 */
export interface UpscaleVideoParams {
  model: TopazUpscaleVideoModel;
  /** Public URL of the source video to upscale. */
  source_video_url: string;
  /** Multiplier for the output resolution (1x, 2x, or 4x). */
  upscale_factor?: UpscaleVideoFactor;
  /** HTTPS callback URL for task completion notification. */
  callback_url?: string;
}

/** Acknowledgement returned by `create()` before the task starts processing. */
export interface TaskCreateResponse {
  id: string;
  status: string;
}

/** URL to an upscaled image. */
export interface Image {
  url: string;
}

/** URL to an upscaled video. */
export interface Video {
  url: string;
}

/** Async image upscaling task result with lifecycle status. */
export interface UpscaleImageResponse {
  id: string;
  status: AsyncTaskStatus;
  /** Upscaled image files; populated once the task completes. */
  images?: Image[];
  error?: string;
  [key: string]: unknown;
}

/** Async video upscaling task result with lifecycle status. */
export interface UpscaleVideoResponse {
  id: string;
  status: AsyncTaskStatus;
  /** Upscaled video files; populated once the task completes. */
  videos?: Video[];
  error?: string;
  [key: string]: unknown;
}
