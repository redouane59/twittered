package io.github.redouane59.twitter.dto.others;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlockResponse {

  private BlockData data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class BlockData {

    private boolean blocking;
  }
}
