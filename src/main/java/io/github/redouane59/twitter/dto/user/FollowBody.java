package io.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FollowBody {

  @JsonProperty("target_user_id")
  private final String targetUserId;

}
