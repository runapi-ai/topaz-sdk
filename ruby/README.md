# Topaz Ruby SDK for RunAPI

The Topaz Ruby SDK is the language-specific package for Topaz on RunAPI. Use this package for image upscale, video upscale, restoration, and production cleanup workflows when your application needs request bodies, task status lookup, and consistent RunAPI errors in Ruby.

This README is the Ruby package guide inside the public `topaz-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/topaz; for API reference, use https://runapi.ai/docs#topaz; for SDK docs, use https://runapi.ai/docs#sdk-topaz.

## Install

```bash
gem install runapi-topaz
```

## Quick start

```ruby
require "runapi-topaz"

client = RunApi::Topaz::Client.new
task = client.upscale_image.create(
  # Pass the Topaz JSON request body from https://runapi.ai/docs#topaz.
)
status = client.upscale_image.get(task.id)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Use Ruby keyword arguments and the `RunApi::Topaz` error classes when building upscaling jobs, Rails workers, or scripts. The available resources are `upscale_image` and `upscale_video`. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

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
