# frozen_string_literal: true

module RunApi
  module Topaz
    module Resources
      # AI-powered video upscaling resource.
      # Supports upscale factors of 1x, 2x, and 4x.
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
          validate_contract!(CONTRACT["upscale-video"], params)
          request(:post, ENDPOINT, body: params)
        end

        def get(id)
          request(:get, "#{ENDPOINT}/#{id}")
        end
      end
    end
  end
end
