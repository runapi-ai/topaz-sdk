import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { UpscaleImageParams, UpscaleImageResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/topaz/upscale_image';

export class UpscaleImage {
  constructor(private readonly http: HttpClient) {}

  async run(params: UpscaleImageParams, options?: RequestOptions & PollingOptions): Promise<UpscaleImageResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<UpscaleImageResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
  }

  async create(params: UpscaleImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  async get(id: string, options?: RequestOptions): Promise<UpscaleImageResponse> {
    return this.http.request<UpscaleImageResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
