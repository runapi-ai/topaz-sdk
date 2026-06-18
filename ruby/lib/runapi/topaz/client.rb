# frozen_string_literal: true

module RunApi
  module Topaz
    # Topaz AI upscaling client for increasing image and video resolution.
    #
    # @example
    #   client = RunApi::Topaz::Client.new(api_key: "sk-...")
    #   result = client.upscale_image.run(
    #     model: "topaz-upscale-image",
    #     source_image_url: "https://example.com/photo.jpg",
    #     upscale_factor: 2
    #   )
    #   puts result.images.first.url
    class Client < RunApi::Core::Client
      # @return [Resources::UpscaleImage] AI-powered image upscaling (1x, 2x, 4x, 8x).
      attr_reader :upscale_image
      # @return [Resources::UpscaleVideo] AI-powered video upscaling (1x, 2x, 4x).
      attr_reader :upscale_video

      def initialize(api_key: nil, **options)
        super
        @upscale_image = Resources::UpscaleImage.new(http)
        @upscale_video = Resources::UpscaleVideo.new(http)
      end
    end
  end
end
