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
		Model:         "topaz-image-upscale",
		ImageURL:      "https://static.aiquickdraw.com/tools/example/1762752805607_mErUj1KR.png",
		UpscaleFactor: "4",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/topaz/upscale_image" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "topaz-image-upscale" || body["upscale_factor"] != "4" {
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
		Model:    "topaz-video-upscale",
		VideoURL: "https://file.aiquickdraw.com/custom-page/akr/section-images/1758166466095hvbwkrpw.mp4",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/topaz/upscale_video" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "topaz-video-upscale" {
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
