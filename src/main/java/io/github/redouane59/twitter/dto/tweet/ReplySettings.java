package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ReplySettings {

  @JsonProperty("everyone")
  EVERYONE("everyone"),
  @JsonProperty("mentionedUsers")
  MENTIONED_USERS("mentionedUsers"),
  @JsonProperty("following")
  FOLLOWING("following"),
  @JsonProperty("other")
  OTHER("other"),
  @JsonProperty("verified")
  VERIFIED("verified");

  public final String label;

  ReplySettings(String label) {
    this.label = label;
  }
}
