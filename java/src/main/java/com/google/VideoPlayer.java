package com.google;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Scanner;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;

  private HashMap<String, VideoPlaylist> playlists;
  private ArrayList<String> playlistNames;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.playlists = new HashMap<>();
    this.playlistNames = new ArrayList<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    List<Video> videos = videoLibrary.getVideos();
    String output = "";
    ArrayList<String> details = new ArrayList<>();

    for(Video temp : videos){
      details.add("\n  " + temp.getTitle()
                  + " (" + temp.getVideoId() + ") "
                  + temp.getTags().toString().replace(",", "")
                  + temp.getFlaggedStatus());
    }
    details.sort(Comparator.comparing(String::toString));

    for(String temp : details){
      output += temp;
    }

    System.out.println("Here's a list of all available videos: " + output);
  }

  public void playVideo(String videoId) {
    try{
      Video v = videoLibrary.getVideo(videoId);
      if(v.getIsFlagged()){
        System.out.println(
          "Cannot play video: Video is currently flagged"
          + " (reason: " + v.getFlagReason() + ")");
      }else{
        if(videoLibrary.getCurrentVideo() != null){
          System.out.println(
            "Stopping video: " + videoLibrary.getCurrentVideo().getTitle() + "\n"
            + "Playing video: " + v.getTitle());
        }else{
          System.out.println("Playing video: " + v.getTitle());
        }
        videoLibrary.setCurrentVideo(v);
        videoLibrary.setIsPaused(false);
      }
    }
    catch(Exception e){
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if(videoLibrary.getCurrentVideo() != null){
      System.out.println("Stopping video: " + videoLibrary.getCurrentVideo().getTitle());
      videoLibrary.setCurrentVideo(null);
    }else{
      System.out.println("Cannot stop video: No video is currently playing");  
    }
  }

  public void playRandomVideo() {
    Random rand = new Random();
    List<Video> videos = videoLibrary.getVideos();
    Video newRandomVideo = videos.get(rand.nextInt(videos.size()));
    int noFlaggedVideos = 0;
    for(Video temp : videos){
      if(temp.getIsFlagged()){
        noFlaggedVideos++;
      }
    }
    if(noFlaggedVideos == videoLibrary.getVideos().size()){
      System.out.println("No videos available");
    }else{
      while(newRandomVideo.getIsFlagged()){
        newRandomVideo = videos.get(rand.nextInt(videos.size()));
      }
      if(videoLibrary.getCurrentVideo() != null){
        System.out.println(
            "Stopping video: " + videoLibrary.getCurrentVideo().getTitle() + "\n"
            + "Playing video: " + newRandomVideo.getTitle());
      }else{
        System.out.println("Playing video: " + newRandomVideo.getTitle());
      }
      videoLibrary.setCurrentVideo(newRandomVideo);
      videoLibrary.setIsPaused(false);
    }
  }

  public void pauseVideo() {
    if(videoLibrary.getCurrentVideo() != null){
      if(videoLibrary.getIsPaused()){
        System.out.println("Video already paused: " + videoLibrary.getCurrentVideo().getTitle());
      }else{
        System.out.println("Pausing video: " + videoLibrary.getCurrentVideo().getTitle());
      }
    }else{
      System.out.println("Cannot pause video: No video is currently playing");
    }
    videoLibrary.setIsPaused(true);
  }

  public void continueVideo() {
    if(videoLibrary.getCurrentVideo() != null){
      if(videoLibrary.getIsPaused() == true){
        System.out.println("Continuing video: " + videoLibrary.getCurrentVideo().getTitle());
        videoLibrary.setIsPaused(false);
      }else{
        System.out.println("Cannot continue video: Video is not paused");
      }
    }else{
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    Video currentVideo = videoLibrary.getCurrentVideo();
    if(videoLibrary.getCurrentVideo() != null){
      
      if(videoLibrary.getIsPaused() == false){
        System.out.println(
          "Currently playing: " + currentVideo.getTitle() + " ("
          + currentVideo.getVideoId() + ") "
          + currentVideo.getTags().toString().replace(",", ""));
      }else{
        System.out.println(
          "Currently playing: " + currentVideo.getTitle() + " ("
          + currentVideo.getVideoId() + ") "
          + currentVideo.getTags().toString().replace(",", "")
          + " - PAUSED");
      }
      
      
    }else{
      System.out.println("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {
    if(playlistName.contains(" ")){
      System.out.println("Cannot create playlist: Name must have no whitespace");
      return;
    }
    if(playlistNames.toString().toUpperCase().contains(playlistName.toUpperCase())){
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    }else{
      System.out.println("Successfully created new playlist: " + playlistName);
      playlistNames.add(playlistName);
      VideoPlaylist vp = new VideoPlaylist(playlistName);
      playlists.put(playlistName.toUpperCase(), vp);
    }

  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    Video v = videoLibrary.getVideo(videoId);
    if(playlists.containsKey(playlistName.toUpperCase())){
      VideoPlaylist vp = playlists.get(playlistName.toUpperCase());
      if(videoLibrary.getVideos().contains(v)){
        if(v.getIsFlagged()){
          System.out.println(
            "Cannot add video to " + playlistName + ": Video is currently flagged"
            + " (reason: " + v.getFlagReason() + ")");
          return;
        }
        if(vp.getVideos().contains(v)){
          System.out.println(
            "Cannot add video to "
            + playlistName + ": Video already added");
        }else{
          System.out.println(
            "Added video to "
            + playlistName + ": " + v.getTitle());
          vp.addVideo(v);
        }
      }else{
        System.out.println(
          "Cannot add video to "
          + playlistName + ": Video does not exist");
      }
    }else{
      System.out.println(
        "Cannot add video to "
        + playlistName + ": Playlist does not exist");
    }
  }

  public void showAllPlaylists() {
    String output = "";
    if(playlists.isEmpty()){
      System.out.println("No playlists exist yet");
    }else{
      playlistNames.sort(Comparator.comparing(String::toString));
      for(String temp : playlistNames){
        output += "\n  " + temp;
      }
      System.out.println(
        "Showing all playlists:"
        + output);
    }

  }

  public void showPlaylist(String playlistName) {
    String output = "";
    if(playlists.containsKey(playlistName.toUpperCase())){
      VideoPlaylist vp = playlists.get(playlistName.toUpperCase());
      if(vp.getVideos().isEmpty()){
        System.out.println(
          "Showing playlist: " + playlistName
          + "\n  No videos here yet");
      }else{
        for(Video temp : vp.getVideos()){
          String title = temp.getTitle();
          String videoId = temp.getVideoId();
          String flaggedStatus = temp.getFlaggedStatus();
          String tags = temp.getTags().toString().replace(",", "");
          output += "\n  " + title + " (" + videoId + ") " + tags + flaggedStatus;
        }
        System.out.println(
          "Showing playlist: " + playlistName
          + output);
      }
    }else{
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    Video v = videoLibrary.getVideo(videoId);
    if(playlists.containsKey(playlistName.toUpperCase())){
      VideoPlaylist vp = playlists.get(playlistName.toUpperCase());
      if(videoLibrary.getVideos().contains(v)){
        if(vp.getVideos().contains(v)){
          System.out.println("Removed video from " + playlistName + ": " + v.getTitle());
          vp.removeVideo(v);
        }else{
          System.out.println(
            "Cannot remove video from " 
            + playlistName + ": Video is not in playlist");
        }
      }else{
        System.out.println(
          "Cannot remove video from " 
          + playlistName  + ": Video does not exist");
      }
    }else{
      System.out.println(
        "Cannot remove video from " 
        + playlistName  + ": Playlist does not exist");
    }
    
  }

  public void clearPlaylist(String playlistName) {
    if(playlists.containsKey(playlistName.toUpperCase())){
      VideoPlaylist vp = playlists.get(playlistName.toUpperCase());
      vp.clearPlaylist();
      System.out.println("Successfully removed all videos from " + playlistName);
    }else{
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void deletePlaylist(String playlistName) {
    if(playlists.containsKey(playlistName.toUpperCase())){
      playlists.remove(playlistName.toUpperCase());
      for(int i = 0; i < playlistNames.size(); i++){
        if(playlistName.toUpperCase().equals(playlistNames.get(i).toUpperCase())){
          playlistNames.remove(i);
        }
      }
      System.out.println("Deleted playlist: " + playlistName);
    }else{
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void searchVideos(String searchTerm) {
    List<Video> videos = videoLibrary.getVideos();
    ArrayList<String> searchList = new ArrayList<>();
    for(Video temp : videos){
      if(!temp.getIsFlagged()){
        if(temp.getTitle().toUpperCase().contains(searchTerm.toUpperCase())){
          String details = temp.getTitle() + " (" + temp.getVideoId() + ") " 
                          + temp.getTags().toString().replace(",", "");
          searchList.add(details);
        }
      }
    }
    outputOptions(searchList, searchTerm);
  }

  public void searchVideosWithTag(String videoTag) {
    List<Video> videos = videoLibrary.getVideos();
    ArrayList<String> searchList = new ArrayList<>();
    for(Video temp : videos){
      if(!temp.getIsFlagged()){
        for(String temp2 : temp.getTags()){
          if(temp2.toUpperCase().contains(videoTag.toUpperCase())){
            String details = temp.getTitle() + " (" + temp.getVideoId() + ") " 
                          + temp.getTags().toString().replace(",", "");
            searchList.add(details);
            break;
          }
        }
      }
    }
    outputOptions(searchList, videoTag);
  }

  public void outputOptions(ArrayList<String> searchList, String searchTerm){
    String output = "";
    if(searchList.isEmpty()){
      System.out.println("No search results for " + searchTerm);
    }else{
      searchList.sort(Comparator.comparing(String::toString));
      for(int i = 0; i < searchList.size(); i++){
        output += "\n  " + (i + 1) + ") " + searchList.get(i);
      }
      System.out.println(
        "Here are the results for " + searchTerm + ": "
        + output);
      Scanner scanner = new Scanner(System.in);
      System.out.println(
        "Would you like to play any of the above? "
        + "If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      String input = scanner.nextLine();
      try{
        for(int i = 0; i < searchList.size(); i++){
          if(Integer.parseInt(input) == i + 1){
            String details = searchList.get(i);
            String videoId = details.substring(details.indexOf("(")+1, details.indexOf(")"));
            playVideo(videoId);
          }
        }
      }
      catch(Exception e){
        return;
      }
    }
  }

  public void flagVideo(String videoId) {
    Video v = videoLibrary.getVideo(videoId);
    if(videoLibrary.getVideos().contains(v)){
      if(v.getIsFlagged()){
        System.out.println("Cannot flag video: Video is already flagged");
      }else{
        if(videoLibrary.getCurrentVideo() == v){
          stopVideo();
        }
        v.setIsFlagged(true);
        v.setFlaggedStatus(" - FLAGGED (reason: Not supplied)");
        v.setFlagReason("Not supplied");
        System.out.println("Successfully flagged video: " + v.getTitle() + " (reason: Not supplied)");
      }
    }else{
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void flagVideo(String videoId, String reason) {
    Video v = videoLibrary.getVideo(videoId);
    if(videoLibrary.getVideos().contains(v)){
      if(v.getIsFlagged()){
        System.out.println("Cannot flag video: Video is already flagged");
      }else{
        if(videoLibrary.getCurrentVideo() == v){
          stopVideo();
        }
        v.setIsFlagged(true);
        v.setFlaggedStatus(" - FLAGGED (reason: " + reason + ")");
        v.setFlagReason(reason);
        System.out.println("Successfully flagged video: " + v.getTitle() + " (reason: " + reason + ")");
      }
    }else{
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void allowVideo(String videoId) {
    Video v = videoLibrary.getVideo(videoId);
    if(videoLibrary.getVideos().contains(v)){
      if(v.getIsFlagged()){
        v.setIsFlagged(false);
        v.setFlaggedStatus("");
        v.setFlagReason("");
        System.out.println("Successfully removed flag from video: " + v.getTitle());
      }else{
        System.out.println("Cannot remove flag from video: Video is not flagged");
      }
    }else{
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
  }
  
}