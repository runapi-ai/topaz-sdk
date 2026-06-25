CONTRACT = {
    "upscale-image": {
        "models": ["topaz-upscale-image"],
        "fields_by_model": {
            "topaz-upscale-image": {
                "source_image_url": {
                    "required": True
                },
                "upscale_factor": {
                    "enum": [1, 2, 4, 8],
                    "required": True,
                    "type": "integer"
                }
            }
        }
    },
    "upscale-video": {
        "models": ["topaz-upscale-video"],
        "fields_by_model": {
            "topaz-upscale-video": {
                "source_video_url": {
                    "required": True
                },
                "upscale_factor": {
                    "enum": [1, 2, 4],
                    "type": "integer"
                }
            }
        }
    }
}
