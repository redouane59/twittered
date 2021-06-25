package io.github.redouane59.twitter.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class HiddenResponse {

  private HiddenData data;

  @NoArgsConstructor
  @AllArgsConstructor
  @Setter
  @Getter
  public static class HiddenData {

    private boolean hidden;
  }

}
