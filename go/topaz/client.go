package topaz

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	imageUpscalesPath = "/api/v1/topaz/image_upscales"
	videoUpscalesPath = "/api/v1/topaz/video_upscales"
)

type Client struct {
	ImageUpscales *ImageUpscales
	VideoUpscales *VideoUpscales
}

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

func NewClientWithHTTP(httpClient core.HTTPClient) *Client {
	return &Client{
		ImageUpscales: &ImageUpscales{http: httpClient},
		VideoUpscales: &VideoUpscales{http: httpClient},
	}
}

type ImageUpscales struct{ http core.HTTPClient }

func (r *ImageUpscales) Create(ctx context.Context, params ImageUpscaleParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, imageUpscalesPath, core.CompactParams(params), requestOptions)
}

func (r *ImageUpscales) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageUpscaleResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageUpscaleResponse](ctx, r.http, core.ResourcePath(imageUpscalesPath, id), requestOptions)
}

func (r *ImageUpscales) Run(ctx context.Context, params ImageUpscaleParams, opts ...option.RequestOption) (*ImageUpscaleResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageUpscaleResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

type VideoUpscales struct{ http core.HTTPClient }

func (r *VideoUpscales) Create(ctx context.Context, params VideoUpscaleParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, videoUpscalesPath, core.CompactParams(params), requestOptions)
}

func (r *VideoUpscales) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoUpscaleResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[VideoUpscaleResponse](ctx, r.http, core.ResourcePath(videoUpscalesPath, id), requestOptions)
}

func (r *VideoUpscales) Run(ctx context.Context, params VideoUpscaleParams, opts ...option.RequestOption) (*VideoUpscaleResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*VideoUpscaleResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}
