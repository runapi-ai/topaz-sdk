# frozen_string_literal: true

module RunApi
  module Topaz
    # Response models for the Topaz upscaling API.
    module Types
      # URL to an upscaled image.
      class Image < RunApi::Core::BaseModel
        optional :url, String
      end

      # URL to an upscaled video.
      class Video < RunApi::Core::BaseModel
        optional :url, String
      end

      # Async image upscaling task result with lifecycle status.
      class UpscaleImageResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :images, [-> { Image }]
        optional :error, String
      end

      # Async video upscaling task result with lifecycle status.
      class UpscaleVideoResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :videos, [-> { Video }]
        optional :error, String
      end

      # Narrowed response returned by +run+ once polling confirms completion.
      # Images are guaranteed present.
      class CompletedUpscaleImageResponse < UpscaleImageResponse
        required :images, [-> { Image }]
      end

      # Narrowed response returned by +run+ once polling confirms completion.
      # Videos are guaranteed present.
      class CompletedUpscaleVideoResponse < UpscaleVideoResponse
        required :videos, [-> { Video }]
      end
    end
  end
end
