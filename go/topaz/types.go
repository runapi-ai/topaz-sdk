package topaz

// TaskStatus is the async task lifecycle state (e.g. "processing", "completed", "failed").
type TaskStatus string

// UpscaleImageParams configures image upscaling.
// UpscaleFactor is required and accepts 1, 2, 4, or 8.
type UpscaleImageParams struct {
	Model          string `json:"model" help:"required; model slug"`
	SourceImageURL string `json:"source_image_url" help:"required; public input image URL"`
	UpscaleFactor  int    `json:"upscale_factor" help:"required; upscale factor"`
	CallbackURL    string `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

// UpscaleVideoParams configures video upscaling.
// UpscaleFactor is optional and accepts 1, 2, or 4.
type UpscaleVideoParams struct {
	Model          string `json:"model" help:"required; model slug"`
	SourceVideoURL string `json:"source_video_url" help:"required; public input video URL"`
	UpscaleFactor  int    `json:"upscale_factor,omitempty" help:"optional; upscale factor"`
	CallbackURL    string `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

// AsyncTaskResponse carries the task ID, lifecycle status, and error for all Topaz async operations.
type AsyncTaskResponse struct {
	ID     string     `json:"id"`
	Status TaskStatus `json:"status"`
	Error  string     `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return string(r.Status) }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

// Image holds a URL to an upscaled image.
type Image struct {
	URL string `json:"url"`
}

// Video holds a URL to an upscaled video.
type Video struct {
	URL string `json:"url"`
}

// UpscaleImageResponse is the result of an image upscaling task.
type UpscaleImageResponse struct {
	AsyncTaskResponse
	Images []Image `json:"images,omitempty"`
}

// UpscaleVideoResponse is the result of a video upscaling task.
type UpscaleVideoResponse struct {
	AsyncTaskResponse
	Videos []Video `json:"videos,omitempty"`
}
