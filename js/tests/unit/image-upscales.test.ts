import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { ImageUpscales } from '../../src/resources/image-upscales';
import type { ImageUpscaleResponse, TaskCreateResponse } from '../../src/types';

describe('ImageUpscales', () => {
  const mockHttp: HttpClient = {
    request: vi.fn(),
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates image upscale requests with the expected body', async () => {
    const mockResponse: TaskCreateResponse = { id: 'img-task-123', status: 'processing' };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const resource = new ImageUpscales(mockHttp);
    const result = await resource.create({
      model: 'topaz/image-upscale',
      image_url: 'https://static.aiquickdraw.com/tools/example/1762752805607_mErUj1KR.png',
      upscale_factor: '4',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/topaz/image_upscales', {
      body: {
        model: 'topaz/image-upscale',
        image_url: 'https://static.aiquickdraw.com/tools/example/1762752805607_mErUj1KR.png',
        upscale_factor: '4',
      },
    });
    expect(result).toEqual(mockResponse);
  });

  it('gets image upscale status', async () => {
    const mockResponse: ImageUpscaleResponse = {
      id: 'img-task-123',
      status: 'completed',
      images: [{ url: 'https://tempfile.runapi.ai/topaz/result.png' }],
    };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const resource = new ImageUpscales(mockHttp);
    const result = await resource.get('img-task-123');

    expect(mockHttp.request).toHaveBeenCalledWith('GET', '/api/v1/topaz/image_upscales/img-task-123', {});
    expect(result).toEqual(mockResponse);
  });
});
