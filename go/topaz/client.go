package topaz

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	upscaleImagePath = "/api/v1/topaz/upscale_image"
	upscaleVideoPath = "/api/v1/topaz/upscale_video"
)

type Client struct {
	UpscaleImage *UpscaleImage
	UpscaleVideo *UpscaleVideo
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
		UpscaleImage: &UpscaleImage{http: httpClient},
		UpscaleVideo: &UpscaleVideo{http: httpClient},
	}
}

type UpscaleImage struct{ http core.HTTPClient }

func (r *UpscaleImage) Create(ctx context.Context, params UpscaleImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, upscaleImagePath, core.CompactParams(params), requestOptions)
}

func (r *UpscaleImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*UpscaleImageResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[UpscaleImageResponse](ctx, r.http, core.ResourcePath(upscaleImagePath, id), requestOptions)
}

func (r *UpscaleImage) Run(ctx context.Context, params UpscaleImageParams, opts ...option.RequestOption) (*UpscaleImageResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*UpscaleImageResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

type UpscaleVideo struct{ http core.HTTPClient }

func (r *UpscaleVideo) Create(ctx context.Context, params UpscaleVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, upscaleVideoPath, core.CompactParams(params), requestOptions)
}

func (r *UpscaleVideo) Get(ctx context.Context, id string, opts ...option.RequestOption) (*UpscaleVideoResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[UpscaleVideoResponse](ctx, r.http, core.ResourcePath(upscaleVideoPath, id), requestOptions)
}

func (r *UpscaleVideo) Run(ctx context.Context, params UpscaleVideoParams, opts ...option.RequestOption) (*UpscaleVideoResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*UpscaleVideoResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}
