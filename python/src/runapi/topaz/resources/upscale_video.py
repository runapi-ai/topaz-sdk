"""Topaz upscale-video resource."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import Resource, RequestOptions

from ..contract_gen import CONTRACT
from ..types import (
    CompletedUpscaleVideoResponse,
    UpscaleVideoResponse,
)


class UpscaleVideo(Resource):
    """Upscale videos with Topaz."""

    ENDPOINT = "/api/v1/topaz/upscale_video"

    RESPONSE_CLASS = UpscaleVideoResponse
    COMPLETED_RESPONSE_CLASS = CompletedUpscaleVideoResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Upscale a video and poll until it completes.

        Args:
            **params: video upscale parameters (model, ...).

        Returns:
            The completed (narrowed) video upscale response.
        """
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))

    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create a video upscale task and return immediately with an id.

        Args:
            **params: video upscale parameters (model, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_contract(CONTRACT["upscale-video"], compacted)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        """Fetch the current status of a video upscale task.

        Args:
            id: The task id returned by ``create``.

        Returns:
            The current task status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)
