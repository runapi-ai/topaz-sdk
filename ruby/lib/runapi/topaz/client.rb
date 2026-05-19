# frozen_string_literal: true

module RunApi
  module Topaz
    class Client
      attr_reader :upscale_image, :upscale_video

      def initialize(api_key: nil, **options)
        @api_key = Core::Auth.resolve_api_key(api_key)

        client_options = Core::ClientOptions.new(api_key: @api_key, **options)
        http = client_options.http_client || Core::HttpClient.new(client_options)
        @upscale_image = Resources::UpscaleImage.new(http)
        @upscale_video = Resources::UpscaleVideo.new(http)
      end
    end
  end
end
