# Changelog

## [js/v0.3.0](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.3.0), [ruby/v0.3.0](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.3.0), [go/v0.3.0](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.3.0), [python/v0.3.0](https://github.com/runapi-ai/topaz-sdk/releases/tag/python%2Fv0.3.0), [java/v0.2.0](https://github.com/runapi-ai/topaz-sdk/releases/tag/java%2Fv0.2.0) - 2026-09-30

### Changed
- Send request parameters to the service without local validation. Model ids and parameter values the service supports work without an SDK upgrade; static types and enum constants remain for completion.
  Migration: Invalid parameters now fail with the validation error built from the service's 400 response, including its status and message, instead of a validation error raised locally before the request. The error type is unchanged: `ValidationError` in JavaScript, Python, and Ruby, `ValidationException` in Java and PHP, and `ErrValidation` in Go.


## [js/v0.2.12](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.2.12), [go/v0.2.12](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.2.12) - 2026-09-28

### Added
- Return usage.cost as a float USD amount on completed async Task query and webhook envelopes.

### Removed
- Remove the public Task billing object from Task envelopes.
  Migration: Read usage.cost on completed Task envelopes. Create, processing, and failed envelopes omit usage.


## [js/v0.2.11](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.2.11), [ruby/v0.2.12](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.2.12), [go/v0.2.11](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.2.11), [python/v0.2.3](https://github.com/runapi-ai/topaz-sdk/releases/tag/python%2Fv0.2.3), [java/v0.1.3](https://github.com/runapi-ai/topaz-sdk/releases/tag/java%2Fv0.1.3) - 2026-09-04

### Fixed
- Restrict image upscale requests to the supported 1x, 2x, and 4x factors.


## [js/v0.2.10](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.2.10), [ruby/v0.2.11](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.2.11), [go/v0.2.10](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.2.10), [python/v0.2.2](https://github.com/runapi-ai/topaz-sdk/releases/tag/python%2Fv0.2.2), [java/v0.1.2](https://github.com/runapi-ai/topaz-sdk/releases/tag/java%2Fv0.1.2) - 2026-08-25

### Fixed
- Restrict image upscale requests to the supported 1x, 2x, and 4x factors.


## [ruby/v0.2.10](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.2.10) - 2026-08-18

### Changed
- Allow Ruby clients to install the core SDK release that adds persistent Files and multipart Uploads alongside this model SDK.


## [js/v0.2.9](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.2.9), [ruby/v0.2.9](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.2.9), [go/v0.2.9](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.2.9), [python/v0.2.1](https://github.com/runapi-ai/topaz-sdk/releases/tag/python%2Fv0.2.1) - 2026-07-28

### Changed
- Describe image and video upscaling request fields and validate existing requirements.


## [go/v0.2.8](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.2.8) - 2026-07-28

### Added
- Expose persisted billing facts on task responses.

## [js/v0.2.8](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.2.8) - 2026-07-28

### Added
- Type task billing facts on task responses.

## [ruby/v0.2.8](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.2.8) - 2026-07-28

### Added
- Expose live pricing through the shared core SDK.


## [python/v0.2.0](https://github.com/runapi-ai/topaz-sdk/releases/tag/python%2Fv0.2.0) - 2026-07-24

### Added
- Expose shared Files, Account, and Pricing resources plus typed Task Billing Facts through the Provider Client.


## [js/v0.2.7](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.2.7), [ruby/v0.2.7](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.2.7), [go/v0.2.7](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.2.7) - 2026-07-02

### Fixed
- Request validation now derives allowed values (aspect ratios, output resolutions, formats) from the RunAPI request contract, so valid requests are no longer rejected client-side.
- Corrected field names and widened enum coverage for image generation endpoints.
- Documented reference image URL parameters where supported.

## [java/v0.1.1](https://github.com/runapi-ai/topaz-sdk/releases/tag/java%2Fv0.1.1) - 2026-06-25

### Fixed
- Fixed Java retry handling for Retry-After response headers.
- Fixed Java contract validation for action-level conditional rules.
- Refreshed Java SDK metadata for v0.1.1.

## [java/v0.1.0](https://github.com/runapi-ai/topaz-sdk/releases/tag/java%2Fv0.1.0) - 2026-06-24

### Added
- Publish `ai.runapi:runapi-topaz` for Java SDK consumers.
- Include typed Java builders, synchronous client resources, sources, and Javadocs.

## [js/v0.2.6](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.2.6), [ruby/v0.2.6](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.2.6), [go/v0.2.6](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.2.6), [python/v0.1.0](https://github.com/runapi-ai/topaz-sdk/releases/tag/python%2Fv0.1.0) - 2026-06-18

### Changed
- Per-method documentation for all resource methods

## [js/v0.2.5](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.2.5), [ruby/v0.2.5](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.2.5), [go/v0.2.5](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.2.5) - 2026-06-01

### Changed
- Align SDK with upstream Input Contract and public API vocabulary changes
- Update endpoint definitions and field constraints

## [js/v0.2.4](https://github.com/runapi-ai/topaz-sdk/releases/tag/js%2Fv0.2.4), [ruby/v0.2.4](https://github.com/runapi-ai/topaz-sdk/releases/tag/ruby%2Fv0.2.4), [go/v0.2.4](https://github.com/runapi-ai/topaz-sdk/releases/tag/go%2Fv0.2.4) - 2026-05-22

### Changed
- Publish JavaScript, Ruby, and Go SDK artifacts for topaz with per-language GitHub release tags.
- Refresh public README metadata.

## [v0.2.1](https://github.com/runapi-ai/topaz-sdk/releases/tag/v0.2.1) - 2026-05-19

Initial release.
