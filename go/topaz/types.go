package topaz

type TaskStatus string

type ImageUpscaleParams struct {
	Model         string `json:"model" help:"required; topaz/image-upscale"`
	ImageURL      string `json:"image_url" help:"required; public input image URL"`
	UpscaleFactor string `json:"upscale_factor" help:"required; 1, 2, 4, or 8"`
	CallbackURL   string `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

type VideoUpscaleParams struct {
	Model         string `json:"model" help:"required; topaz/video-upscale"`
	VideoURL      string `json:"video_url" help:"required; public input video URL"`
	UpscaleFactor string `json:"upscale_factor,omitempty" help:"optional; 1, 2, or 4"`
	CallbackURL   string `json:"callback_url,omitempty" help:"optional; webhook URL"`
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

type ImageUpscaleResponse struct {
	AsyncTaskResponse
	Images []Image `json:"images,omitempty"`
}

type VideoUpscaleResponse struct {
	AsyncTaskResponse
	Videos []Video `json:"videos,omitempty"`
}
