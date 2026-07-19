# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Topaz::Client do
  let(:client) { described_class.new(api_key: "test-key") }

  it "exposes upscale_image resource" do
    expect(client.upscale_image).to be_a(RunApi::Topaz::Resources::UpscaleImage)
  end

  it "exposes upscale_video resource" do
    expect(client.upscale_video).to be_a(RunApi::Topaz::Resources::UpscaleVideo)
  end
end
