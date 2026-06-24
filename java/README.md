# Topaz Java SDK for RunAPI

[![Maven Central](https://img.shields.io/maven-central/v/ai.runapi/runapi-topaz)](https://central.sonatype.com/artifact/ai.runapi/runapi-topaz)

The Topaz Java SDK is the language-specific package for Topaz on RunAPI. Use it when your Java application needs typed builders, strict request validation, task status lookup, local polling helpers, file uploads, account helpers, and consistent RunAPI errors for Topaz workflows.

This README is the Java package guide inside the public `topaz-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/topaz; for API reference, use https://runapi.ai/docs#topaz; for SDK docs, use https://runapi.ai/docs#sdk-topaz.

## Requirements

The Java SDK targets Java 8 bytecode and is tested on Java 8, 11, 17, and 21.

## Install

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

Use the BOM when multiple RunAPI Java modules are installed:

```kotlin
dependencies {
  implementation(platform("ai.runapi:runapi-bom:0.1.0"))
  implementation("ai.runapi:runapi-topaz")
}
```

Maven BOM:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>ai.runapi</groupId>
      <artifactId>runapi-bom</artifactId>
      <version>0.1.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

## Quick Start

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

The client builder reads `RUNAPI_API_KEY` when `.apiKey(...)` is omitted. Set `RUNAPI_BASE_URL` or `.baseUrl(...)` only when using a non-default RunAPI endpoint.

## Task Lifecycle

Most media endpoints are asynchronous. `create(params)` submits a task and returns its id, `get(id)` fetches the latest task state, and `run(params)` creates the task and polls until it reaches a terminal state. Synchronous resources expose `run(params)` only.

```java
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.topaz.TopazClient;
import ai.runapi.topaz.types.UpscaleImageParams;
import ai.runapi.topaz.types.UpscaleImageResponse;
import ai.runapi.topaz.types.UpscaleImageModel;

TopazClient client = TopazClient.builder()
    .apiKey(System.getenv("RUNAPI_API_KEY"))
    .build();

UpscaleImageParams params = UpscaleImageParams.builder()
    .model(UpscaleImageModel.TOPAZ_UPSCALE_IMAGE)
    .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
    .upscaleFactor(2)
    .build();

TaskCreateResponse task = client.upscaleImage().create(params);
UpscaleImageResponse status = client.upscaleImage().get(task.getId());
```

## Files

Each model package depends transitively on `ai.runapi:runapi-core`, so you can upload files through any model client.

```java
import ai.runapi.core.files.FileCreateParams;
import ai.runapi.core.files.FileUploadResponse;
import java.nio.file.Paths;

FileUploadResponse uploaded = client.files().create(
    FileCreateParams.fromPath(Paths.get("input.png"))
        .fileName("input.png")
        .build()
);
```

URL and base64 sources use the same entry point:

```java
FileUploadResponse fromUrl = client.files().create(
    FileCreateParams.fromUrl("https://example.com/input.png")
        .fileName("input.png")
        .build()
);

FileUploadResponse fromBase64 = client.files().create(
    FileCreateParams.fromBase64("iVBORw0KGgo...")
        .fileName("input.png")
        .build()
);
```

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Request Options

```java
import ai.runapi.core.RequestOptions;
import java.time.Duration;

RequestOptions options = RequestOptions.builder()
    .pollingInterval(Duration.ofSeconds(3))
    .pollingMaxWait(Duration.ofMinutes(20))
    .maxRetries(0)
    .build();
```

Per-request options can override timeout, retries, headers, and polling behavior.

## Error Handling

All SDK errors extend `RunApiException`.

```java
import ai.runapi.core.errors.RateLimitException;
import ai.runapi.core.errors.RunApiException;
import ai.runapi.core.errors.ValidationException;

try {
  client.upscaleImage().run(params);
} catch (ValidationException error) {
  System.err.println(error.getMessage());
} catch (RateLimitException error) {
  System.err.println(error.getRetryAfter());
} catch (RunApiException error) {
  System.err.println(error.getCode() + " " + error.getStatusCode());
}
```

## Links

- Model page: https://runapi.ai/models/topaz
- SDK docs: https://runapi.ai/docs#sdk-topaz
- Product docs: https://runapi.ai/docs#topaz
- Pricing and rate limits: https://runapi.ai/models/topaz/upscale-image
- Full catalog: https://runapi.ai/models
- Repository: https://github.com/runapi-ai/topaz-sdk

## License

Licensed under the Apache License, Version 2.0.
