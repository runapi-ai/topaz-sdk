import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { UpscaleVideo } from '../../src/resources/upscale-video';
import type { TaskCreateResponse, UpscaleVideoResponse } from '../../src/types';

describe('UpscaleVideo', () => {
  const mockHttp: HttpClient = {
    request: vi.fn(),
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates video upscale requests with optional factor omitted', async () => {
    const mockResponse: TaskCreateResponse = { id: 'vid-task-123', status: 'processing' };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const resource = new UpscaleVideo(mockHttp);
    const result = await resource.create({
      model: 'topaz-upscale-video',
      source_video_url: 'https://cdn.runapi.ai/public/samples/video-lowres.mp4',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/topaz/upscale_video', {
      body: {
        model: 'topaz-upscale-video',
        source_video_url: 'https://cdn.runapi.ai/public/samples/video-lowres.mp4',
      },
    });
    expect(result).toEqual(mockResponse);
  });

  it('gets video upscale status', async () => {
    const mockResponse: UpscaleVideoResponse = {
      id: 'vid-task-123',
      status: 'completed',
      videos: [{ url: 'https://cdn-video.runapi.ai/topaz/result.mp4' }],
    };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const resource = new UpscaleVideo(mockHttp);
    const result = await resource.get('vid-task-123');

    expect(mockHttp.request).toHaveBeenCalledWith('GET', '/api/v1/topaz/upscale_video/vid-task-123', {});
    expect(result).toEqual(mockResponse);
  });
});
