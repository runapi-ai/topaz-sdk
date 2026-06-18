# Topaz API Python SDK for RunAPI

The topaz api Python SDK is the language-specific package for Topaz on RunAPI. Use this topaz api package for image upscale, video upscale, restoration, and production cleanup flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in Python.

This topaz api README is the Python package guide inside the public `topaz-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/topaz; for API reference, use https://runapi.ai/docs#topaz; for SDK docs, use https://runapi.ai/docs#sdk-topaz.

## Install

```bash
pip install runapi-topaz
```

## Quick start

```python
from runapi.topaz import TopazClient

client = TopazClient()  # reads RUNAPI_API_KEY, or pass api_key="sk-..."

task = client.upscale_image.create(
    model="topaz-upscale-image",
    source_image_url="https://example.com/in.jpg",
    upscale_factor=4,
)
status = client.upscale_image.get(task.id)

video = client.upscale_video.create(
    model="topaz-upscale-video",
    source_video_url="https://example.com/in.mp4",
    upscale_factor=2,
)
```

Use `create` to submit a task and return quickly, `get` to fetch the latest task state, and `run` when a script should create and poll until completion:

```python
result = client.upscale_image.run(
    model="topaz-upscale-image",
    source_image_url="https://example.com/in.jpg",
    upscale_factor=4,
)
print(result.images[0].url)
```

In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Pass parameters as keyword arguments and catch the `runapi.topaz` error classes when building upscaling jobs or scripts. The available resources are `upscale_image` and `upscale_video`. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

## Links

- Model page: https://runapi.ai/models/topaz
- SDK docs: https://runapi.ai/docs#sdk-topaz
- Product docs: https://runapi.ai/docs#topaz
- Pricing and rate limits: https://runapi.ai/models/topaz/upscale-image
- Provider comparison: https://runapi.ai/providers/topaz
- Full catalog: https://runapi.ai/models
- Repository: https://github.com/runapi-ai/topaz-sdk

## License

Licensed under the Apache License, Version 2.0.
