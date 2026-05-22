<p align="center">
  <a href="https://github.com/runapi-ai/topaz">
    <h3 align="center">Topaz API Skill for RunAPI</h3>
  </a>
</p>

<p align="center">
  Install this agent skill, inspect Topaz fields, then run jobs through the RunAPI CLI.
</p>

<p align="center">
  <a href="https://runapi.ai/models/topaz"><strong>Model Reference</strong></a> · <a href="https://github.com/runapi-ai/cli"><strong>CLI</strong></a> · <a href="https://github.com/runapi-ai/topaz-sdk"><strong>SDK</strong></a>
</p>

<div align="center">

[![skills.sh](https://www.skills.sh/b/runapi-ai/topaz)](https://www.skills.sh/runapi-ai/topaz/topaz)
[![ClawHub](https://img.shields.io/badge/ClawHub-runapi--topaz-111827)](https://clawhub.ai/runapi-ai/runapi-topaz)
[![License](https://img.shields.io/github/license/runapi-ai/topaz)](https://github.com/runapi-ai/topaz/blob/main/LICENSE)

</div>
<br/>

Upscale images and video with Topaz AI-powered enhancement. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents integrate Topaz through RunAPI.

The canonical agent file is `skills/topaz/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/topaz -g
```

Or paste this prompt to your AI agent:

```text
Install the topaz skill for me:

1. Clone https://github.com/runapi-ai/topaz
2. Copy the skills/topaz/ directory into your
   user-level skills directory (e.g. ~/.claude/skills/
   for Claude Code, ~/.codex/skills/ for Codex).
3. Verify that SKILL.md is present.
4. Confirm the install path when done.
```

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
