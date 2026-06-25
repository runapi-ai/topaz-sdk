# frozen_string_literal: true

module RunApi
  module Topaz
    CONTRACT = {
      "upscale-image" => {
        "models" => ["topaz-upscale-image"],
        "fields_by_model" => {
          "topaz-upscale-image" => {
            "source_image_url" => {
              "required" => true
            },
            "upscale_factor" => {
              "enum" => [1, 2, 4, 8],
              "required" => true,
              "type" => "integer"
            }
          }
        }
      },
      "upscale-video" => {
        "models" => ["topaz-upscale-video"],
        "fields_by_model" => {
          "topaz-upscale-video" => {
            "source_video_url" => {
              "required" => true
            },
            "upscale_factor" => {
              "enum" => [1, 2, 4],
              "type" => "integer"
            }
          }
        }
      }
    }.freeze
  end
end
