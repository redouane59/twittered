package io.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Jacksonized
public class UserActionResponse {

  private FollowData data;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Jacksonized
  public static class FollowData {

    boolean muting;
    private boolean following;
    @JsonProperty("pending_follow")
    private boolean pendingFollow;

  }
}
