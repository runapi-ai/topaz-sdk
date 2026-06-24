package topaz

import (
	"context"
	"encoding/json"
	"testing"

	"github.com/runapi-ai/core-sdk/go/core"
)

type stubHTTPClient struct {
	method   string
	path     string
	body     any
	response json.RawMessage
}

func (s *stubHTTPClient) Request(_ context.Context, method, path string, opts *core.HTTPRequestOptions) (json.RawMessage, error) {
	s.method = method
	s.path = path
	if opts != nil {
		s.body = opts.Body
	}
	return s.response, nil
}

func TestUpscaleImageCreate(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"id":"img-task-123","status":"processing"}`)}
	client := NewClientWithHTTP(stub)
	resp, err := client.UpscaleImage.Create(context.Background(), UpscaleImageParams{
		Model:          "topaz-upscale-image",
		SourceImageURL: "https://cdn.runapi.ai/public/samples/upscale.jpg",
		UpscaleFactor:  4,
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/topaz/upscale_image" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "topaz-upscale-image" || body["source_image_url"] != "https://cdn.runapi.ai/public/samples/upscale.jpg" || body["upscale_factor"] != float64(4) {
		t.Fatalf("unexpected body: %#v", body)
	}
	if _, ok := body["image_url"]; ok {
		t.Fatalf("unexpected body: %#v", body)
	}
	if resp.ID != "img-task-123" {
		t.Fatalf("unexpected task id: %s", resp.ID)
	}
}

func TestUpscaleImageGet(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"id":"img-task-123","status":"completed","images":[{"url":"https://tempfile.runapi.ai/topaz/result.png"}]}`)}
	client := NewClientWithHTTP(stub)
	resp, err := client.UpscaleImage.Get(context.Background(), "img-task-123")
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "GET" || stub.path != "/api/v1/topaz/upscale_image/img-task-123" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	if resp.Images[0].URL != "https://tempfile.runapi.ai/topaz/result.png" {
		t.Fatalf("unexpected image url: %s", resp.Images[0].URL)
	}
}

func TestUpscaleVideoCreate(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"id":"vid-task-123","status":"processing"}`)}
	client := NewClientWithHTTP(stub)
	resp, err := client.UpscaleVideo.Create(context.Background(), UpscaleVideoParams{
		Model:          "topaz-upscale-video",
		SourceVideoURL: "https://cdn.runapi.ai/public/samples/video-lowres.mp4",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/topaz/upscale_video" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "topaz-upscale-video" || body["source_video_url"] != "https://cdn.runapi.ai/public/samples/video-lowres.mp4" {
		t.Fatalf("unexpected body: %#v", body)
	}
	if _, ok := body["upscale_factor"]; ok {
		t.Fatalf("expected request body to omit upscale_factor key: %#v", body)
	}
	if _, ok := body["video_url"]; ok {
		t.Fatalf("unexpected body: %#v", body)
	}
	if resp.ID != "vid-task-123" {
		t.Fatalf("unexpected task id: %s", resp.ID)
	}
}

func TestUpscaleVideoGet(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"id":"vid-task-123","status":"completed","videos":[{"url":"https://cdn-video.runapi.ai/topaz/result.mp4"}]}`)}
	client := NewClientWithHTTP(stub)
	resp, err := client.UpscaleVideo.Get(context.Background(), "vid-task-123")
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "GET" || stub.path != "/api/v1/topaz/upscale_video/vid-task-123" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	if resp.Videos[0].URL != "https://cdn-video.runapi.ai/topaz/result.mp4" {
		t.Fatalf("unexpected video url: %s", resp.Videos[0].URL)
	}
}
