import pytest

from runapi.core import config
from runapi.core.errors import AuthenticationError, ValidationError
from runapi.topaz import TopazClient
from runapi.topaz.resources.upscale_image import UpscaleImage
from runapi.topaz.resources.upscale_video import UpscaleVideo
from runapi.topaz.types import (
    CompletedUpscaleImageResponse,
    CompletedUpscaleVideoResponse,
    UpscaleImageResponse,
    UpscaleVideoResponse,
)


class FakeHttp:
    def __init__(self, *responses):
        self._responses = list(responses)
        self.calls = []

    def request(self, method, path, body=None, options=None):
        self.calls.append((method, path, body))
        if self._responses:
            return self._responses.pop(0)
        return {"id": "task_1", "status": "pending"}


@pytest.fixture(autouse=True)
def reset_config(monkeypatch):
    monkeypatch.delenv("RUNAPI_API_KEY", raising=False)
    monkeypatch.setattr(config, "api_key", None)
    yield


# --- auth -----------------------------------------------------------------


def test_accepts_api_key_parameter():
    assert isinstance(TopazClient(api_key="k", http_client=FakeHttp()), TopazClient)


def test_falls_back_to_global(monkeypatch):
    monkeypatch.setattr(config, "api_key", "global-key")
    assert isinstance(TopazClient(http_client=FakeHttp()), TopazClient)


def test_falls_back_to_env(monkeypatch):
    monkeypatch.setenv("RUNAPI_API_KEY", "env-key")
    assert isinstance(TopazClient(http_client=FakeHttp()), TopazClient)


def test_raises_without_api_key():
    with pytest.raises(AuthenticationError, match="API key is required"):
        TopazClient()


# --- injection / accessors ------------------------------------------------


def test_uses_injected_http_client():
    fake = FakeHttp()
    client = TopazClient(api_key="k", http_client=fake)
    assert client.upscale_image._http is fake
    assert client.upscale_video._http is fake


def test_exposes_resource_accessors():
    client = TopazClient(api_key="k", http_client=FakeHttp())
    assert isinstance(client.upscale_image, UpscaleImage)
    assert isinstance(client.upscale_video, UpscaleVideo)


# --- request shapes -------------------------------------------------------


def test_create_posts_compacted_body():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = TopazClient(api_key="k", http_client=fake)
    result = client.upscale_image.create(
        model="topaz-upscale-image",
        source_image_url="https://x/y.jpg",
        upscale_factor=4,
        note=None,
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/topaz/upscale_image",
            {"model": "topaz-upscale-image", "source_image_url": "https://x/y.jpg", "upscale_factor": 4},
        ),
    ]
    assert isinstance(result, UpscaleImageResponse)


def test_get_fetches_by_id():
    fake = FakeHttp({"id": "t1", "status": "processing"})
    client = TopazClient(api_key="k", http_client=fake)
    client.upscale_image.get("t1")
    assert fake.calls == [("get", "/api/v1/topaz/upscale_image/t1", None)]


def test_video_create_posts_compacted_body():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = TopazClient(api_key="k", http_client=fake)
    result = client.upscale_video.create(
        model="topaz-upscale-video",
        source_video_url="https://x/y.mp4",
        upscale_factor=2,
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/topaz/upscale_video",
            {"model": "topaz-upscale-video", "source_video_url": "https://x/y.mp4", "upscale_factor": 2},
        ),
    ]
    assert isinstance(result, UpscaleVideoResponse)


def test_run_narrows_completed_image_type():
    fake = FakeHttp(
        {"id": "t1", "status": "pending"},
        {"id": "t1", "status": "completed", "images": [{"url": "https://x/y.png"}]},
    )
    client = TopazClient(api_key="k", http_client=fake)
    result = client.upscale_image.run(
        model="topaz-upscale-image", source_image_url="https://x/y.jpg", upscale_factor=4
    )
    assert isinstance(result, CompletedUpscaleImageResponse)
    assert result.images[0].url == "https://x/y.png"


def test_run_narrows_completed_video_type():
    fake = FakeHttp(
        {"id": "t1", "status": "pending"},
        {"id": "t1", "status": "completed", "videos": [{"url": "https://x/y.mp4"}]},
    )
    client = TopazClient(api_key="k", http_client=fake)
    result = client.upscale_video.run(
        model="topaz-upscale-video", source_video_url="https://x/y.mp4", upscale_factor=2
    )
    assert isinstance(result, CompletedUpscaleVideoResponse)
    assert result.videos[0].url == "https://x/y.mp4"


# --- validation -----------------------------------------------------------


def test_image_requires_model():
    client = TopazClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="model is required"):
        client.upscale_image.create(source_image_url="https://x/y.jpg", upscale_factor=4)


def test_image_requires_source_image_url():
    client = TopazClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_image_url is required"):
        client.upscale_image.create(model="topaz-upscale-image", upscale_factor=4)


def test_image_requires_upscale_factor():
    client = TopazClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="upscale_factor is required"):
        client.upscale_image.create(model="topaz-upscale-image", source_image_url="https://x/y.jpg")


def test_image_rejects_bad_factor():
    client = TopazClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="upscale_factor must be one of: 1, 2, 4, 8"):
        client.upscale_image.create(
            model="topaz-upscale-image", source_image_url="https://x/y.jpg", upscale_factor=3
        )


def test_video_requires_model():
    client = TopazClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="model is required"):
        client.upscale_video.create(source_video_url="https://x/y.mp4")


def test_video_requires_source_video_url():
    client = TopazClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_video_url is required"):
        client.upscale_video.create(model="topaz-upscale-video")


def test_video_upscale_factor_optional():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = TopazClient(api_key="k", http_client=fake)
    client.upscale_video.create(model="topaz-upscale-video", source_video_url="https://x/y.mp4")
    assert fake.calls == [
        ("post", "/api/v1/topaz/upscale_video", {"model": "topaz-upscale-video", "source_video_url": "https://x/y.mp4"}),
    ]


def test_video_rejects_bad_factor():
    client = TopazClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="upscale_factor must be one of: 1, 2, 4"):
        client.upscale_video.create(
            model="topaz-upscale-video", source_video_url="https://x/y.mp4", upscale_factor=8
        )
