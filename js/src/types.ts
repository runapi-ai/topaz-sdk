import type { AsyncTaskStatus } from '@runapi.ai/core';

export type TopazImageUpscaleModel = 'topaz/image-upscale';
export type TopazVideoUpscaleModel = 'topaz/video-upscale';
export type ImageUpscaleFactor = '1' | '2' | '4' | '8';
export type VideoUpscaleFactor = '1' | '2' | '4';

export interface ImageUpscaleParams {
  model: TopazImageUpscaleModel;
  image_url: string;
  upscale_factor: ImageUpscaleFactor;
  callback_url?: string;
}

export interface VideoUpscaleParams {
  model: TopazVideoUpscaleModel;
  video_url: string;
  upscale_factor?: VideoUpscaleFactor;
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

export interface ImageUpscaleResponse {
  id: string;
  status: AsyncTaskStatus;
  images?: Image[];
  error?: string;
  [key: string]: unknown;
}

export interface VideoUpscaleResponse {
  id: string;
  status: AsyncTaskStatus;
  videos?: Video[];
  error?: string;
  [key: string]: unknown;
}
