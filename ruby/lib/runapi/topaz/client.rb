# frozen_string_literal: true

module RunApi
  module Topaz
    class Client
      attr_reader :image_upscales, :video_upscales

      def initialize(api_key: nil, **options)
        @api_key = Core::Auth.resolve_api_key(api_key)

        client_options = Core::ClientOptions.new(api_key: @api_key, **options)
        http = client_options.http_client || Core::HttpClient.new(client_options)
        @image_upscales = Resources::ImageUpscales.new(http)
        @video_upscales = Resources::VideoUpscales.new(http)
      end
    end
  end
end
