"""Topaz client."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import ClientOptions, HttpClient, resolve_api_key

from .resources.upscale_image import UpscaleImage
from .resources.upscale_video import UpscaleVideo


class TopazClient:
    """Topaz image and video upscale client.

    Example::

        client = TopazClient(api_key="sk-...")
        result = client.upscale_image.run(
            model="topaz-upscale-image",
            source_image_url="https://example.com/in.jpg",
            upscale_factor=4,
        )
    """

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        resolved_api_key = resolve_api_key(api_key)
        client_options = ClientOptions(api_key=resolved_api_key, **options)
        http = client_options.http_client or HttpClient(client_options)
        self.upscale_image = UpscaleImage(http)
        self.upscale_video = UpscaleVideo(http)
