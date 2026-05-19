# frozen_string_literal: true

module RunApi
  module Topaz
    module Types
      UPSCALE_IMAGE_MODEL = "topaz-image-upscale"
      UPSCALE_VIDEO_MODEL = "topaz-video-upscale"
      UPSCALE_IMAGE_FACTORS = %w[1 2 4 8].freeze
      UPSCALE_VIDEO_FACTORS = %w[1 2 4].freeze

      class Image < RunApi::Core::BaseModel
        optional :url, String
      end

      class Video < RunApi::Core::BaseModel
        optional :url, String
      end

      class UpscaleImageResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :images, [ -> { Image } ]
        optional :error, String
      end

      class UpscaleVideoResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :videos, [ -> { Video } ]
        optional :error, String
      end

      class CompletedUpscaleImageResponse < UpscaleImageResponse
        required :images, [ -> { Image } ]
      end

      class CompletedUpscaleVideoResponse < UpscaleVideoResponse
        required :videos, [ -> { Video } ]
      end
    end
  end
end
