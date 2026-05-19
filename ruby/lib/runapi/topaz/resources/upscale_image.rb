# frozen_string_literal: true

module RunApi
  module Topaz
    module Resources
      class UpscaleImage
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/topaz/upscale_image"

        RESPONSE_CLASS = Types::UpscaleImageResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedUpscaleImageResponse

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
          raise Core::ValidationError, "model is required" unless param(params, :model) == Types::UPSCALE_IMAGE_MODEL
          raise Core::ValidationError, "image_url is required" unless param(params, :image_url)

          factor = param(params, :upscale_factor)
          raise Core::ValidationError, "upscale_factor is required" unless factor
          return if Types::UPSCALE_IMAGE_FACTORS.include?(factor.to_s)

          raise Core::ValidationError, "upscale_factor must be one of: #{Types::UPSCALE_IMAGE_FACTORS.join(", ")}"
        end
      end
    end
  end
end
