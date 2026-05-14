import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { ImageUpscaleParams, ImageUpscaleResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/topaz/image_upscales';

export class ImageUpscales {
  constructor(private readonly http: HttpClient) {}

  async run(params: ImageUpscaleParams, options?: RequestOptions & PollingOptions): Promise<ImageUpscaleResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<ImageUpscaleResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
  }

  async create(params: ImageUpscaleParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  async get(id: string, options?: RequestOptions): Promise<ImageUpscaleResponse> {
    return this.http.request<ImageUpscaleResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
