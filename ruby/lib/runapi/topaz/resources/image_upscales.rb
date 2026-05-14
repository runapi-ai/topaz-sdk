# frozen_string_literal: true

module RunApi
  module Topaz
    module Resources
      class ImageUpscales
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/topaz/image_upscales"

        RESPONSE_CLASS = Types::ImageUpscaleResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedImageUpscaleResponse

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
          raise Core::ValidationError, "model is required" unless param(params, :model) == Types::IMAGE_UPSCALE_MODEL
          raise Core::ValidationError, "image_url is required" unless param(params, :image_url)

          factor = param(params, :upscale_factor)
          raise Core::ValidationError, "upscale_factor is required" unless factor
          return if Types::IMAGE_UPSCALE_FACTORS.include?(factor.to_s)

          raise Core::ValidationError, "upscale_factor must be one of: #{Types::IMAGE_UPSCALE_FACTORS.join(", ")}"
        end
      end
    end
  end
end
