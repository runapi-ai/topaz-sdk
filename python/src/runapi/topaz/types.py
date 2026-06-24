"""Topaz response models."""

from __future__ import annotations

from runapi.core import BaseModel, TaskResponse, optional, required


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
