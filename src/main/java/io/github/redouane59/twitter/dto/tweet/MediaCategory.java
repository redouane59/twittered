package io.github.redouane59.twitter.dto.tweet;

public enum MediaCategory {

  AMPLIFY_VIDEO("amplify_video"),
  TWEET_GIF("tweet_gif"),
  TWEET_IMAGE("tweet_image"),
  TWEET_VIDEO("tweet_video");

  public final String label;

  MediaCategory(String label) {
    this.label = label;
  }

}
