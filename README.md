<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/topaz-sdk">Topaz API SDK for RunAPI</a>
</h3>

<p align="center">
  Topaz API SDKs for JavaScript, Python, Ruby, Go, and Java on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/topaz)](https://www.npmjs.com/package/@runapi.ai/topaz)
[![PyPI](https://img.shields.io/pypi/v/runapi-topaz)](https://pypi.org/project/runapi-topaz/)
[![RubyGems](https://img.shields.io/gem/v/runapi-topaz)](https://rubygems.org/gems/runapi-topaz)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/topaz-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/topaz-sdk/go)
[![Maven Central](https://img.shields.io/maven-central/v/ai.runapi/runapi-topaz)](https://central.sonatype.com/artifact/ai.runapi/runapi-topaz)
[![License](https://img.shields.io/github/license/runapi-ai/topaz-sdk)](https://github.com/runapi-ai/topaz-sdk/blob/main/LICENSE)

</div>
<br/>

The Topaz API SDK packages JavaScript, Python, Ruby, Go, and Java clients for Topaz on RunAPI. Use it for image and video upscaling workflows when your app needs typed request builders, predictable task polling, file upload helpers, account helpers, and consistent RunAPI errors.

Topaz is listed in the RunAPI model catalog at https://runapi.ai/models/topaz. Variant pages below carry pricing, rate-limit, and commercial-usage details. The public `topaz-sdk` repository groups the language packages, examples, CI, and release tags for this model.

## Install

```bash
npm install @runapi.ai/topaz
pip install runapi-topaz
gem install runapi-topaz
go get github.com/runapi-ai/topaz-sdk/go@latest
```

Gradle:

```kotlin
dependencies {
  implementation("ai.runapi:runapi-topaz:0.1.0")
}
```

Maven:

```xml
<dependency>
  <groupId>ai.runapi</groupId>
  <artifactId>runapi-topaz</artifactId>
  <version>0.1.0</version>
</dependency>
```

Use the Java BOM when installing multiple RunAPI Java modules:

```kotlin
dependencies {
  implementation(platform("ai.runapi:runapi-bom:0.1.0"))
  implementation("ai.runapi:runapi-topaz")
}
```

## What you can build

- Build apps, agent workflows, batch jobs, and production services around Topaz requests.
- Install only the language package your app needs while keeping one model-specific repository for docs and releases.
- Use `create` for submit-only jobs, `get` for status lookup, and `run` for submit-and-poll scripts.
- Upload local files, URL files, or base64 files through shared RunAPI file helpers.
- Handle validation, authentication, rate limits, insufficient credits, task failures, and polling timeouts through RunAPI SDK errors.

## Java quick start

```java
import ai.runapi.topaz.TopazClient;
import ai.runapi.topaz.types.UpscaleImageParams;
import ai.runapi.topaz.types.CompletedUpscaleImageResponse;
import ai.runapi.topaz.types.UpscaleImageModel;

TopazClient client = TopazClient.builder()
    .apiKey(System.getenv("RUNAPI_API_KEY"))
    .build();

CompletedUpscaleImageResponse result = client.upscaleImage().run(
    UpscaleImageParams.builder()
        .model(UpscaleImageModel.TOPAZ_UPSCALE_IMAGE)
        .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
        .upscaleFactor(2)
        .build()
);
```

Java packages target Java 8 bytecode and are tested on Java 8, 11, 17, and 21. Each model artifact depends on `ai.runapi:runapi-core`, so application code normally installs only `ai.runapi:runapi-topaz`.

## Task lifecycle

Most media endpoints are asynchronous. `create()` submits a task and returns its id, `get(id)` fetches the latest task state, and `run(params)` creates the task and polls until it reaches a terminal state. In web request handlers, prefer `create()` plus webhook or later `get()` polling so the server does not hold a worker open.

## Repository layout

- `js/` publishes `@runapi.ai/topaz`.
- `python/` publishes `runapi-topaz`.
- `ruby/` publishes `runapi-topaz`.
- `go/` publishes `github.com/runapi-ai/topaz-sdk/go`.
- `java/` publishes `ai.runapi:runapi-topaz` and uses `ai.runapi:runapi-core`.

## Public links

- Model page: https://runapi.ai/models/topaz
- SDK docs: https://runapi.ai/docs#sdk-topaz
- Product docs: https://runapi.ai/docs#topaz
- SDK repository: https://github.com/runapi-ai/topaz-sdk
- Skill repository: https://github.com/runapi-ai/topaz
- Provider comparison: https://runapi.ai/providers/topaz
- Full catalog: https://runapi.ai/models

## Pricing and variants

Use the most specific Topaz variant page for pricing, rate limits, and commercial usage:
- [Image upscale](https://runapi.ai/models/topaz/upscale-image)
- [Video upscale](https://runapi.ai/models/topaz/upscale-video)

Default pricing link for the Topaz SDK: https://runapi.ai/models/topaz/upscale-image

## File storage

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## FAQ

### Which package should I install for Topaz work?

Install the model package for your language: `@runapi.ai/topaz` on npm, `runapi-topaz` on PyPI, `runapi-topaz` on RubyGems, `github.com/runapi-ai/topaz-sdk/go`, or `ai.runapi:runapi-topaz`. Install core SDK packages only when you are building shared SDK infrastructure.

### Where should public links point?

Primary Topaz links point to https://runapi.ai/models/topaz. Pricing and usage-policy links point to variant pages such as https://runapi.ai/models/topaz/upscale-image. Provider comparisons point to https://runapi.ai/providers/topaz, and broad browsing points to https://runapi.ai/models.

## License

Licensed under the Apache License, Version 2.0.
