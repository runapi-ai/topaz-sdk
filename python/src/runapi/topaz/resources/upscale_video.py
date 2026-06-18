"""Topaz upscale-video resource."""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..types import (
    UPSCALE_VIDEO_FACTORS,
    UPSCALE_VIDEO_MODEL,
    CompletedUpscaleVideoResponse,
    UpscaleVideoResponse,
)


class UpscaleVideo(Resource):
    """Upscale videos with Topaz."""

    ENDPOINT = "/api/v1/topaz/upscale_video"

    RESPONSE_CLASS = UpscaleVideoResponse
    COMPLETED_RESPONSE_CLASS = CompletedUpscaleVideoResponse

    def run(self, **params: Any) -> Any:
        """Upscale a video and poll until it completes.

        Args:
            **params: video upscale parameters (model, ...).

        Returns:
            The completed (narrowed) video upscale response.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create a video upscale task and return immediately with an id.

        Args:
            **params: video upscale parameters (model, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of a video upscale task.

        Args:
            id: The task id returned by ``create``.

        Returns:
            The current task status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        if params.get("model") != UPSCALE_VIDEO_MODEL:
            raise ValidationError("model is required")
        if not params.get("source_video_url"):
            raise ValidationError("source_video_url is required")

        factor = params.get("upscale_factor")
        if not factor or factor in UPSCALE_VIDEO_FACTORS:
            return

        raise ValidationError(
            f"upscale_factor must be one of: {', '.join(str(f) for f in UPSCALE_VIDEO_FACTORS)}"
        )
