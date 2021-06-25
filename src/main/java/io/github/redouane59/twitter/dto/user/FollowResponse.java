package com.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FollowResponse {

  private FollowData data;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class FollowData {

    private boolean following;
    @JsonProperty("pending_follow")
    private boolean pending_follow;

  }
}
