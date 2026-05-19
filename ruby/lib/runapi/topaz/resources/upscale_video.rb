# frozen_string_literal: true

module RunApi
  module Topaz
    module Resources
      class UpscaleVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/topaz/upscale_video"

        RESPONSE_CLASS = Types::UpscaleVideoResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedUpscaleVideoResponse

        def initialize(http)
          @http = http
        end

        def run(**params)
          task = create(**params)
          poll_until_complete { get(task.id) }
        end

        def create(**params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params)
        end

        def get(id)
          request(:get, "#{ENDPOINT}/#{id}")
        end

        private

        def validate_params!(params)
          raise Core::ValidationError, "model is required" unless param(params, :model) == Types::UPSCALE_VIDEO_MODEL
          raise Core::ValidationError, "video_url is required" unless param(params, :video_url)

          factor = param(params, :upscale_factor)
          return unless factor && !Types::UPSCALE_VIDEO_FACTORS.include?(factor.to_s)

          raise Core::ValidationError, "upscale_factor must be one of: #{Types::UPSCALE_VIDEO_FACTORS.join(", ")}"
        end
      end
    end
  end
end
