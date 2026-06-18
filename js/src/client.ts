import { BaseClient, type ClientOptions } from '@runapi.ai/core';
import { UpscaleImage } from './resources/upscale-image';
import { UpscaleVideo } from './resources/upscale-video';

/**
 * Topaz AI upscaling client for increasing image and video resolution.
 *
 * @example
 * ```typescript
 * import { TopazClient } from '@runapi.ai/topaz';
 * const client = new TopazClient({ apiKey: 'sk-...' });
 * const result = await client.upscaleImage.run({
 *   model: 'topaz-upscale-image',
 *   source_image_url: 'https://example.com/photo.jpg',
 *   upscale_factor: 2,
 * });
 * console.log(result.images![0].url);
 * ```
 */
export class TopazClient extends BaseClient {
  /** AI-powered image upscaling; supports 1x, 2x, 4x, and 8x factors. */
  public readonly upscaleImage: UpscaleImage;
  /** AI-powered video upscaling; supports 1x, 2x, and 4x factors. */
  public readonly upscaleVideo: UpscaleVideo;

  constructor(options: ClientOptions = {}) {
    super(options);
    this.upscaleImage = new UpscaleImage(this.http);
    this.upscaleVideo = new UpscaleVideo(this.http);
  }
}
