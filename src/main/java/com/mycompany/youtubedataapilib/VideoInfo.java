package com.mycompany.youtubedataapilib;

import java.io.Serializable;

/**
 *
 * @author renan
 */
public class VideoInfo implements Serializable {

    private String videoId;
    private String thumbnailUrl;
    private String title;

    public VideoInfo() {
    }

    public VideoInfo(String videoId, String thumbnailUrl, String title) {
        this.videoId = videoId;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
