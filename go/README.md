# Topaz API Go SDK for RunAPI

The topaz api Go SDK is the language-specific package for Topaz on RunAPI. Use this topaz api package for image upscale, video upscale, restoration, and production cleanup flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in Go.

This topaz api README is the Go package guide inside the public `topaz-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/topaz; for API reference, use https://runapi.ai/docs#topaz; for SDK docs, use https://runapi.ai/docs#sdk-topaz.

## Install

```bash
go get github.com/runapi-ai/topaz-sdk/go@latest
```

## Quick start

```go
import (
  "context"

  "github.com/runapi-ai/topaz-sdk/go/topaz"
)

client, err := topaz.NewClient()
task, err := client.ImageUpscales.Create(context.Background(), topaz.ImageUpscaleParams{
  // Pass the Topaz JSON request body from https://runapi.ai/docs#topaz.
})
status, err := client.ImageUpscales.Get(context.Background(), task.ID)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

## Language notes

Use the public Go module with `github.com/runapi-ai/core-sdk/go` options when building upscaling services, CLIs, or workers. The available resources include image upscales, and video upscales. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

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
