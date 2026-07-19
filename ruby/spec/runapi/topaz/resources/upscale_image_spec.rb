# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Topaz::Resources::UpscaleImage do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/topaz/upscale_image" }

  it "POSTs image upscale requests" do
    params = {
      model: "topaz-upscale-image",
      source_image_url: "https://cdn.runapi.ai/public/samples/upscale.jpg",
      upscale_factor: 4
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "img-1")

    result = resource.create(**params)
    expect(result).to be_a(RunApi::Topaz::Types::UpscaleImageResponse)
    expect(result.id).to eq("img-1")
  end

  it "rejects missing upscale_factor" do
    expect {
      resource.create(model: "topaz-upscale-image", source_image_url: "https://cdn.runapi.ai/public/samples/upscale.jpg")
    }.to raise_error(RunApi::Core::ValidationError, /upscale_factor is required/)
  end
end
