import { createHttpClient, type ClientOptions } from '@runapi.ai/core';
import { UpscaleImage } from './resources/upscale-image';
import { UpscaleVideo } from './resources/upscale-video';

export class TopazClient {
  public readonly upscaleImage: UpscaleImage;
  public readonly upscaleVideo: UpscaleVideo;

  constructor(options: ClientOptions = {}) {
    const http = createHttpClient(options);
    this.upscaleImage = new UpscaleImage(http);
    this.upscaleVideo = new UpscaleVideo(http);
  }
}
