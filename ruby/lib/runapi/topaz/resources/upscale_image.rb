# frozen_string_literal: true

module RunApi
  module Topaz
    module Resources
      # AI-powered image upscaling resource.
      # Supports upscale factors of 1x, 2x, 4x, and 8x.
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
          validate_contract!(CONTRACT["upscale-image"], params)
          request(:post, ENDPOINT, body: params)
        end

        def get(id)
          request(:get, "#{ENDPOINT}/#{id}")
        end
      end
    end
  end
end
