package com.example.doanltdd;

public class YoutobeVideos {
    String videoURL;
    public YoutobeVideos(){

    }
    public YoutobeVideos(String videoURL)
    {
        this.videoURL = videoURL;
    }
    public String getVideoURL()
    {
        return videoURL;
    }
    public void setVideoURL(String videourl)
    {
        this.videoURL = videourl;
    }
}
