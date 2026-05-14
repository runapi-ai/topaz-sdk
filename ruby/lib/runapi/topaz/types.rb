# frozen_string_literal: true

module RunApi
  module Topaz
    module Types
      IMAGE_UPSCALE_MODEL = "topaz/image-upscale"
      VIDEO_UPSCALE_MODEL = "topaz/video-upscale"
      IMAGE_UPSCALE_FACTORS = %w[1 2 4 8].freeze
      VIDEO_UPSCALE_FACTORS = %w[1 2 4].freeze

      class Image < RunApi::Core::BaseModel
        optional :url, String
      end

      class Video < RunApi::Core::BaseModel
        optional :url, String
      end

      class ImageUpscaleResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :images, [ -> { Image } ]
        optional :error, String
      end

      class VideoUpscaleResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :videos, [ -> { Video } ]
        optional :error, String
      end

      class CompletedImageUpscaleResponse < ImageUpscaleResponse
        required :images, [ -> { Image } ]
      end

      class CompletedVideoUpscaleResponse < VideoUpscaleResponse
        required :videos, [ -> { Video } ]
      end
    end
  end
end
