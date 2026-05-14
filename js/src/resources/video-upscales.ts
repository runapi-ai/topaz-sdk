import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { TaskCreateResponse, VideoUpscaleParams, VideoUpscaleResponse } from '../types';

const ENDPOINT = '/api/v1/topaz/video_upscales';

export class VideoUpscales {
  constructor(private readonly http: HttpClient) {}

  async run(params: VideoUpscaleParams, options?: RequestOptions & PollingOptions): Promise<VideoUpscaleResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<VideoUpscaleResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
  }

  async create(params: VideoUpscaleParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  async get(id: string, options?: RequestOptions): Promise<VideoUpscaleResponse> {
    return this.http.request<VideoUpscaleResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
