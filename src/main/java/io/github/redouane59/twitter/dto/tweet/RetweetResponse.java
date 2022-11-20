package io.github.redouane59.twitter.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Jacksonized
public class RetweetResponse {

  private RetweetData data;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  @Jacksonized
  public static class RetweetData {

    private boolean retweeted;
  }
}
