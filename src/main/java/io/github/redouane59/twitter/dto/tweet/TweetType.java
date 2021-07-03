package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TweetType {
  @JsonProperty("retweeted")
  RETWEETED("retweeted"),
  @JsonProperty("quoted")
  QUOTED("quoted"),
  @JsonProperty("replied_to")
  REPLIED_TO("replied_to"),
  DEFAULT("default");

  public final String label;

  TweetType(String label) {
    this.label = label;
  }

  public static TweetType valueOfLabel(String label) {
    for (TweetType e : values()) {
      if (e.label.equals(label)) {
        return e;
      }
    }
    return null;
  }
}
