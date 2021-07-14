package io.github.redouane59.twitter.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class RetweetResponse {

  private RetweetData data;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  public static class RetweetData {

    private boolean retweeted;
  }
}
