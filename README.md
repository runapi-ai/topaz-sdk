<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/topaz-sdk">Topaz API SDK for RunAPI</a>
</h3>

<p align="center">
  Topaz API SDKs for JavaScript, Ruby, and Go on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/topaz)](https://www.npmjs.com/package/@runapi.ai/topaz)
[![RubyGems](https://img.shields.io/gem/v/runapi-topaz)](https://rubygems.org/gems/runapi-topaz)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/topaz-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/topaz-sdk/go)
[![License](https://img.shields.io/github/license/runapi-ai/topaz-sdk)](https://github.com/runapi-ai/topaz-sdk/blob/main/LICENSE)

</div>
<br/>

The topaz api SDK packages JavaScript, Ruby, and Go clients for Topaz on RunAPI. Use this topaz api SDK for image upscale, video upscale, restoration, and production cleanup workflows that need typed installs, JSON request bodies, task polling, and consistent RunAPI errors across services.

Topaz belongs to the Topaz catalog on RunAPI. The public model page is https://runapi.ai/models/topaz; variant pages below carry pricing, rate-limit, and commercial-usage details. The public `topaz-sdk` repository groups the JavaScript, Ruby, and Go packages for this model.

## Install

```bash
npm install @runapi.ai/topaz
gem install runapi-topaz
go get github.com/runapi-ai/topaz-sdk/go@latest
```

## What you can build

- Build asset finishing, ecommerce media cleanup, video enhancement, and batch automation with the topaz api SDK.
- Keep one model-specific repository while installing only the language package your app needs.
- Use `create` for submit-only jobs, `get` for status lookup, and `run` for submit-and-poll scripts.
- Handle authentication, validation, rate limits, billing errors, task failures, and polling timeouts through RunAPI SDK errors.

The JavaScript client exposes image upscales, video upscales resources, and the Ruby and Go packages mirror the same RunAPI task lifecycle.

## JavaScript quick start

```typescript
import { TopazClient } from '@runapi.ai/topaz';

const client = new TopazClient();

const task = await client.imageUpscales.create({
  // Pass the Topaz request body documented at https://runapi.ai/docs#topaz.
});

const status = await client.imageUpscales.get(task.id);
```

For short scripts, use `run` with the same JSON body to create the task and wait for completion. For web request handlers, prefer `create` plus webhook or later `get` polling so the server does not hold a worker open.

## Repository layout

- `js/` publishes `@runapi.ai/topaz`.
- `ruby/` publishes `runapi-topaz` when RubyGems publishing resumes.
- `go/` publishes `github.com/runapi-ai/topaz-sdk/go` and depends on `github.com/runapi-ai/core-sdk/go`.

## Public links

- Model page: https://runapi.ai/models/topaz
- SDK docs: https://runapi.ai/docs#sdk-topaz
- Product docs: https://runapi.ai/docs#topaz
- SDK repository: https://github.com/runapi-ai/topaz-sdk
- Skill repository: https://github.com/runapi-ai/topaz
- Provider comparison: https://runapi.ai/providers/topaz
- Full catalog: https://runapi.ai/models

## Pricing and variants

Use the most specific topaz api variant page for pricing, rate limits, and commercial usage:
- [Image upscale](https://runapi.ai/models/topaz/upscale-image)
- [Video upscale](https://runapi.ai/models/topaz/upscale-video)

Default pricing link for the topaz api SDK: https://runapi.ai/models/topaz/upscale-image

## FAQ

### Which package should I install for topaz api work?

Install the model package for your language: `@runapi.ai/topaz`, `runapi-topaz`, or `github.com/runapi-ai/topaz-sdk/go`. Install core SDK packages only when you are building shared SDK infrastructure.

### Where should public links point?

Primary topaz api links point to https://runapi.ai/models/topaz. Pricing and usage-policy links point to variant pages such as https://runapi.ai/models/topaz/upscale-image. Provider comparisons point to https://runapi.ai/providers/topaz, and broad browsing points to https://runapi.ai/models.

## License

Licensed under the Apache License, Version 2.0.
