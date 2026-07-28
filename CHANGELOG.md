# Changelog

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
