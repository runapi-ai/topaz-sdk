import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { UpscaleImageParams, UpscaleImageResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/topaz/upscale_image';

/**
 * Increases image resolution using AI enhancement.
 * Supports upscale factors of 1x, 2x, 4x, and 8x.
 */
export class UpscaleImage {
  constructor(private readonly http: HttpClient) {}

  /**
   * Create an upscale image task and wait until complete.
   * @param params Upscale image parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed upscale image response.
   */
  async run(params: UpscaleImageParams, options?: RequestOptions & PollingOptions): Promise<UpscaleImageResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<UpscaleImageResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
  }

  /**
   * Create an upscale image task; returns immediately with a task id.
   * @param params Upscale image parameters.
   * @param options Per-request overrides.
   * @returns The task creation result.
   */
  async create(params: UpscaleImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  /**
   * Fetch the current status of an upscale image task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current upscale image task status.
   */
  async get(id: string, options?: RequestOptions): Promise<UpscaleImageResponse> {
    return this.http.request<UpscaleImageResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
