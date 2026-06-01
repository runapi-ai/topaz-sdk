import type { AsyncTaskStatus } from '@runapi.ai/core';

export type TopazUpscaleImageModel = 'topaz-upscale-image';
export type TopazUpscaleVideoModel = 'topaz-upscale-video';
export type UpscaleImageFactor = 1 | 2 | 4 | 8;
export type UpscaleVideoFactor = 1 | 2 | 4;

export interface UpscaleImageParams {
  model: TopazUpscaleImageModel;
  source_image_url: string;
  upscale_factor: UpscaleImageFactor;
  callback_url?: string;
}

export interface UpscaleVideoParams {
  model: TopazUpscaleVideoModel;
  source_video_url: string;
  upscale_factor?: UpscaleVideoFactor;
  callback_url?: string;
}

export interface TaskCreateResponse {
  id: string;
  status: string;
}

export interface Image {
  url: string;
}

export interface Video {
  url: string;
}

export interface UpscaleImageResponse {
  id: string;
  status: AsyncTaskStatus;
  images?: Image[];
  error?: string;
  [key: string]: unknown;
}

export interface UpscaleVideoResponse {
  id: string;
  status: AsyncTaskStatus;
  videos?: Video[];
  error?: string;
  [key: string]: unknown;
}
