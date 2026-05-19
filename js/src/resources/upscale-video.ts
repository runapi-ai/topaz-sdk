import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { TaskCreateResponse, UpscaleVideoParams, UpscaleVideoResponse } from '../types';

const ENDPOINT = '/api/v1/topaz/upscale_video';

export class UpscaleVideo {
  constructor(private readonly http: HttpClient) {}

  async run(params: UpscaleVideoParams, options?: RequestOptions & PollingOptions): Promise<UpscaleVideoResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<UpscaleVideoResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
  }

  async create(params: UpscaleVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  async get(id: string, options?: RequestOptions): Promise<UpscaleVideoResponse> {
    return this.http.request<UpscaleVideoResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
