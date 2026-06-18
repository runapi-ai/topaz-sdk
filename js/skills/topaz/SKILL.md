---
name: topaz
description: Upscale and enhance media with Topaz through RunAPI. Use when the user asks an agent to upscale or enhance images and video with Topaz. Default to the RunAPI CLI for one-off generation; use SDKs only when the user is integrating RunAPI into an app or backend.
documentation: https://runapi.ai/models/topaz.md
provider_page: https://runapi.ai/providers/topaz.md
catalog: https://runapi.ai/models.md
metadata:
  openclaw:
    homepage: https://runapi.ai/models/topaz
    requires:
      bins:
      - runapi
    install:
    - kind: brew
      formula: runapi-ai/tap/runapi
      bins:
      - runapi
    envVars:
    - name: RUNAPI_API_KEY
      required: false
      description: Optional RunAPI API key; agents should prefer environment auth or saved CLI config. Browser login is interactive only.
---

# Topaz on RunAPI

Upscale and enhance media with Topaz through RunAPI. The default path for one-off agent tasks is the `runapi` CLI; SDKs are for application integration.

## Critical: Integration Runtime

- Integration work (app, backend, worker, library, Rails service, Node service, Go service, webhook pipeline, or production codebase) uses the **SDK integration path** for the target language.
- One-off generation, editing, transformation, manual smoke tests, debugging, or user-requested CLI runs use the **CLI path** with the `runapi` binary. For full CLI-specific agent guidance, see https://github.com/runapi-ai/cli-skill.
- Never shell out to the `runapi` CLI as the production runtime integration layer.

## SDK integration path

When integrating Topaz into an app, backend, worker, library, Rails service, Node service, Go service, webhook pipeline, or production workflow, start by checking the current SDK package and official usage. Confirm install commands, client methods (`create`, `get`, `run`), request fields, response shape, and error classes before using CLI help or raw HTTP examples. Use a RunAPI SDK package:

- JavaScript / TypeScript: `@runapi.ai/topaz`
- Ruby: `runapi-topaz`
- Go: `github.com/runapi-ai/topaz-sdk/go`

## CLI path

The `runapi` binary is the one-off and manual testing runtime dependency. For full CLI-specific agent guidance, see https://github.com/runapi-ai/cli-skill. Run `runapi auth status` first. For agents and headless runs, prefer `RUNAPI_API_KEY` or import it into saved config with `printf '%s' "$RUNAPI_API_KEY" | runapi auth import-token --token -`. Use `runapi login` only when the user explicitly wants interactive browser auth.

Inspect the available commands and request fields with CLI help:

```shell
runapi topaz --help
runapi topaz upscale-image --help
```

Run a one-off task (synchronous — polls until the task completes):

```shell
runapi topaz upscale-image --input-file request.json
```

Submit asynchronously and poll separately:

```shell
runapi topaz upscale-image --async --input-file request.json
runapi wait <task-id> --service topaz --action upscale-image
```

Available commands: `upscale-image`, `upscale-video`.

## Generated file storage

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## References

- Model overview, pricing, and rate limits: https://runapi.ai/models/topaz.md
- Provider comparison: https://runapi.ai/providers/topaz.md
- Full model catalog: https://runapi.ai/models.md

## Variants

- [Image upscale](https://runapi.ai/models/topaz/upscale-image.md)
- [Video upscale](https://runapi.ai/models/topaz/upscale-video.md)
