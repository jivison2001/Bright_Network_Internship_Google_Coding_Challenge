package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {

    private ArrayList<Video> videos;
    private final String name;

    public VideoPlaylist(String n){
        this.videos = new ArrayList<>();
        this.name = n;
    }

    public void addVideo(Video v){
        this.videos.add(v);
    }

    public void removeVideo(Video v){
        this.videos.remove(v);
    }
    public void clearPlaylist(){
        videos.clear();
    }

    public String getName(){
        return name;
    }

    public ArrayList<Video> getVideos(){
        return videos;
    }
}
