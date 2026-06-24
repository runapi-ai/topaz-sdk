import type { HttpClient, PollingOptions, RequestOptions, ActionSchema } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import { contract } from '../contract_gen';
import type { TaskCreateResponse, UpscaleVideoParams, UpscaleVideoResponse } from '../types';

const ENDPOINT = '/api/v1/topaz/upscale_video';

/**
 * Increases video resolution using AI enhancement.
 * Supports upscale factors of 1x, 2x, and 4x.
 */
export class UpscaleVideo {
  constructor(private readonly http: HttpClient) {}

  /**
   * Create an upscale video task and wait until complete.
   * @param params Upscale video parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed upscale video response.
   */
  async run(params: UpscaleVideoParams, options?: RequestOptions & PollingOptions): Promise<UpscaleVideoResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<UpscaleVideoResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
  }

  /**
   * Create an upscale video task; returns immediately with a task id.
   * @param params Upscale video parameters.
   * @param options Per-request overrides.
   * @returns The task creation result.
   */
  async create(params: UpscaleVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    validateParams(contract['upscale-video'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body,
      ...options,
    });
  }

  /**
   * Fetch the current status of an upscale video task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current upscale video task status.
   */
  async get(id: string, options?: RequestOptions): Promise<UpscaleVideoResponse> {
    return this.http.request<UpscaleVideoResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
