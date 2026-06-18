"""Topaz model lists, enums, and response models."""

from __future__ import annotations

from runapi.core import BaseModel, TaskResponse, optional, required

UPSCALE_IMAGE_MODEL = "topaz-upscale-image"
UPSCALE_VIDEO_MODEL = "topaz-upscale-video"
UPSCALE_IMAGE_FACTORS = [1, 2, 4, 8]
UPSCALE_VIDEO_FACTORS = [1, 2, 4]


class Image(BaseModel):
    url = optional(str)


class Video(BaseModel):
    url = optional(str)


class UpscaleImageResponse(TaskResponse):
    """Topaz image upscale task status response."""

    id = required(str)
    status = optional(str, enum=lambda: TaskResponse.Status.ALL)
    images = optional([lambda: Image])
    error = optional(str)


class UpscaleVideoResponse(TaskResponse):
    """Topaz video upscale task status response."""

    id = required(str)
    status = optional(str, enum=lambda: TaskResponse.Status.ALL)
    videos = optional([lambda: Video])
    error = optional(str)


class CompletedUpscaleImageResponse(UpscaleImageResponse):
    """Narrowed response from ``run()`` once polling observes completion."""

    images = required([lambda: Image])


class CompletedUpscaleVideoResponse(UpscaleVideoResponse):
    """Narrowed response from ``run()`` once polling observes completion."""

    videos = required([lambda: Video])
