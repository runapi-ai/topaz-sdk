"""Topaz client."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import ProviderClient

from .resources.upscale_image import UpscaleImage
from .resources.upscale_video import UpscaleVideo


class TopazClient(ProviderClient):
    """Topaz image and video upscale client.

    Example::

        client = TopazClient(api_key="sk-...")
        result = client.upscale_image.run(
            model="topaz-upscale-image",
            source_image_url="https://runapi.ai/in.jpg",
            upscale_factor=4,
        )
    """

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        super().__init__(api_key, **options)
        http = self._http
        self.upscale_image = UpscaleImage(http)
        self.upscale_video = UpscaleVideo(http)
