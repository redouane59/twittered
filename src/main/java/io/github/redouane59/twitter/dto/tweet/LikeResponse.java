package io.github.redouane59.twitter.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class LikeResponse {

  DataLikeResponse data;

  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Jacksonized
  public static class DataLikeResponse {

    private boolean liked;
  }
}
