import { createHttpClient, type ClientOptions } from '@runapi.ai/core';
import { ImageUpscales } from './resources/image-upscales';
import { VideoUpscales } from './resources/video-upscales';

export class TopazClient {
  public readonly imageUpscales: ImageUpscales;
  public readonly videoUpscales: VideoUpscales;

  constructor(options: ClientOptions = {}) {
    const http = createHttpClient(options);
    this.imageUpscales = new ImageUpscales(http);
    this.videoUpscales = new VideoUpscales(http);
  }
}
