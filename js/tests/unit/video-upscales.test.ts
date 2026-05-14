import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { VideoUpscales } from '../../src/resources/video-upscales';
import type { TaskCreateResponse, VideoUpscaleResponse } from '../../src/types';

describe('VideoUpscales', () => {
  const mockHttp: HttpClient = {
    request: vi.fn(),
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates video upscale requests with optional factor omitted', async () => {
    const mockResponse: TaskCreateResponse = { id: 'vid-task-123', status: 'processing' };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const resource = new VideoUpscales(mockHttp);
    const result = await resource.create({
      model: 'topaz/video-upscale',
      video_url: 'https://file.aiquickdraw.com/custom-page/akr/section-images/1758166466095hvbwkrpw.mp4',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/topaz/video_upscales', {
      body: {
        model: 'topaz/video-upscale',
        video_url: 'https://file.aiquickdraw.com/custom-page/akr/section-images/1758166466095hvbwkrpw.mp4',
      },
    });
    expect(result).toEqual(mockResponse);
  });

  it('gets video upscale status', async () => {
    const mockResponse: VideoUpscaleResponse = {
      id: 'vid-task-123',
      status: 'completed',
      videos: [{ url: 'https://cdn-video.runapi.ai/topaz/result.mp4' }],
    };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const resource = new VideoUpscales(mockHttp);
    const result = await resource.get('vid-task-123');

    expect(mockHttp.request).toHaveBeenCalledWith('GET', '/api/v1/topaz/video_upscales/vid-task-123', {});
    expect(result).toEqual(mockResponse);
  });
});
