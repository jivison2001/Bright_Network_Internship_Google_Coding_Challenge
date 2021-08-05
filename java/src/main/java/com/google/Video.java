package com.google;

import java.security.cert.CertPathValidatorException.Reason;
import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean isFlagged;
  private String flaggedStatus;
  private String flagReason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.flaggedStatus = "";
    this.isFlagged = false;
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  public void setIsFlagged(boolean value){
    isFlagged = value;
  }
  
  public boolean getIsFlagged(){
    return isFlagged;
  }

  public String getFlaggedStatus(){
    return flaggedStatus;
  }

  public void setFlaggedStatus(String status){
    flaggedStatus = status;
  }

  public String getFlagReason(){
    return flagReason;
  }

  public void setFlagReason(String reason){
    flagReason = reason;
  }
}
