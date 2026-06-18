// Package topaz provides the Topaz AI upscaling API client for images and videos.
//
//	client, err := topaz.NewClient(option.WithAPIKey("sk-your-api-key"))
//	result, err := client.UpscaleImage.Run(ctx, topaz.UpscaleImageParams{
//	    Model: "topaz-upscale-image", SourceImageURL: "https://example.com/photo.jpg", UpscaleFactor: 2,
//	})
package topaz

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/base"
	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	upscaleImagePath = "/api/v1/topaz/upscale_image"
	upscaleVideoPath = "/api/v1/topaz/upscale_video"
)

// Client provides AI-powered upscaling for both images and videos.
type Client struct {
	base.Base
	UpscaleImage *UpscaleImage
	UpscaleVideo *UpscaleVideo
}

// NewClient creates a Topaz client with the given options.
func NewClient(opts ...option.ClientOption) (*Client, error) {
	resolved, err := option.ResolveClientOptions(opts...)
	if err != nil {
		return nil, err
	}
	httpClient, err := core.NewHTTPClient(resolved)
	if err != nil {
		return nil, err
	}
	return NewClientWithHTTP(httpClient), nil
}

// NewClientWithHTTP creates a Topaz client with a pre-configured HTTP transport.
func NewClientWithHTTP(httpClient core.HTTPClient) *Client {
	return &Client{
		Base:         base.New(httpClient),
		UpscaleImage: &UpscaleImage{http: httpClient},
		UpscaleVideo: &UpscaleVideo{http: httpClient},
	}
}

// UpscaleImage increases image resolution using AI enhancement.
// Supports upscale factors of 1x, 2x, 4x, and 8x.
type UpscaleImage struct{ http core.HTTPClient }

// Create submits an image-upscale task and returns immediately with a task id.
func (r *UpscaleImage) Create(ctx context.Context, params UpscaleImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, upscaleImagePath, core.CompactParams(params), requestOptions)
}

// Get fetches the current status of an image-upscale task by id.
func (r *UpscaleImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*UpscaleImageResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[UpscaleImageResponse](ctx, r.http, core.ResourcePath(upscaleImagePath, id), requestOptions)
}

// Run submits an image-upscale task and polls until it completes.
func (r *UpscaleImage) Run(ctx context.Context, params UpscaleImageParams, opts ...option.RequestOption) (*UpscaleImageResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*UpscaleImageResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// UpscaleVideo increases video resolution using AI enhancement.
// Supports upscale factors of 1x, 2x, and 4x.
type UpscaleVideo struct{ http core.HTTPClient }

// Create submits a video-upscale task and returns immediately with a task id.
func (r *UpscaleVideo) Create(ctx context.Context, params UpscaleVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, upscaleVideoPath, core.CompactParams(params), requestOptions)
}

// Get fetches the current status of a video-upscale task by id.
func (r *UpscaleVideo) Get(ctx context.Context, id string, opts ...option.RequestOption) (*UpscaleVideoResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[UpscaleVideoResponse](ctx, r.http, core.ResourcePath(upscaleVideoPath, id), requestOptions)
}

// Run submits a video-upscale task and polls until it completes.
func (r *UpscaleVideo) Run(ctx context.Context, params UpscaleVideoParams, opts ...option.RequestOption) (*UpscaleVideoResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*UpscaleVideoResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}
