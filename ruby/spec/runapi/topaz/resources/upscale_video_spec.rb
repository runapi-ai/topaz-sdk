# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Topaz::Resources::UpscaleVideo do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/topaz/upscale_video" }

  it "POSTs video upscale requests" do
    params = {
      model: "topaz-upscale-video",
      source_video_url: "https://cdn.runapi.ai/public/samples/video-lowres.mp4",
      upscale_factor: 2
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "vid-1")

    result = resource.create(**params)
    expect(result).to be_a(RunApi::Topaz::Types::UpscaleVideoResponse)
    expect(result.id).to eq("vid-1")
  end

  it "allows omitting upscale_factor" do
    params = {
      model: "topaz-upscale-video",
      source_video_url: "https://cdn.runapi.ai/public/samples/video-lowres.mp4"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "vid-2")

    result = resource.create(**params)
    expect(result.id).to eq("vid-2")
  end

  it "rejects an out-of-range upscale_factor" do
    expect {
      resource.create(
        model: "topaz-upscale-video",
        source_video_url: "https://cdn.runapi.ai/public/samples/video-lowres.mp4",
        upscale_factor: 8
      )
    }.to raise_error(RunApi::Core::ValidationError, /upscale_factor must be one of: 1, 2, 4/)
  end

  it "GETs video upscale results" do
    expect(http).to receive(:request).with(:get, "#{endpoint}/vid-1")
      .and_return("id" => "vid-1", "status" => "completed", "videos" => [{"url" => "https://cdn-video.runapi.ai/topaz/result.mp4"}])

    result = resource.get("vid-1")
    expect(result.videos.first.url).to eq("https://cdn-video.runapi.ai/topaz/result.mp4")
  end
end
