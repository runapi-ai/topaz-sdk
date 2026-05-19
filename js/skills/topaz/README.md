# Topaz API Skill for RunAPI

Upscale images and video with Topaz AI-powered enhancement. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents integrate Topaz through RunAPI.

The canonical agent file is `skills/topaz/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/topaz -g
```

Or manually: clone this repo and copy `skills/topaz/` into your agent's skills directory.

## Quick example

```typescript
import { TopazClient } from '@runapi.ai/topaz';

const client = new TopazClient();
const result = await client.upscaleImage.run({
  model: 'topaz-image-upscale',
  image_url: 'https://cdn.example.com/photo.jpg',
});
```

## Routing

- Model page: https://runapi.ai/models/topaz
- Product docs: https://runapi.ai/docs#topaz
- SDK docs: https://runapi.ai/docs#sdk-topaz
- SDK repository: https://github.com/runapi-ai/topaz-sdk
- Pricing and rate limits: https://runapi.ai/models/topaz/image-upscale
- Provider comparison: https://runapi.ai/providers/topaz
- Browse all RunAPI models and skills: https://runapi.ai/models

## Variants

- [Image upscale](https://runapi.ai/models/topaz/image-upscale)
- [Video upscale](https://runapi.ai/models/topaz/video-upscale)

## Agent rules

- Keep API keys in `RUNAPI_API_KEY` or RunAPI CLI config; never commit secrets.
- Prefer `create`, `get`, and `run` JSON passthrough patterns instead of inventing flags for every model parameter.
- For topaz api pricing, rate-limit, and commercial-usage answers, link to the variant page rather than the repository README.

## License

Licensed under the Apache License, Version 2.0.
