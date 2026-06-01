package topaz

type TaskStatus string

type UpscaleImageParams struct {
	Model          string `json:"model" help:"required; model slug"`
	SourceImageURL string `json:"source_image_url" help:"required; public input image URL"`
	UpscaleFactor  int    `json:"upscale_factor" help:"required; upscale factor"`
	CallbackURL    string `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

type UpscaleVideoParams struct {
	Model          string `json:"model" help:"required; model slug"`
	SourceVideoURL string `json:"source_video_url" help:"required; public input video URL"`
	UpscaleFactor  int    `json:"upscale_factor,omitempty" help:"optional; upscale factor"`
	CallbackURL    string `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

type AsyncTaskResponse struct {
	ID     string     `json:"id"`
	Status TaskStatus `json:"status"`
	Error  string     `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return string(r.Status) }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

type Image struct {
	URL string `json:"url"`
}

type Video struct {
	URL string `json:"url"`
}

type UpscaleImageResponse struct {
	AsyncTaskResponse
	Images []Image `json:"images,omitempty"`
}

type UpscaleVideoResponse struct {
	AsyncTaskResponse
	Videos []Video `json:"videos,omitempty"`
}
