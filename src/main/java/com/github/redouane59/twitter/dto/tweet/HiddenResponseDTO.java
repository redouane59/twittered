package com.github.redouane59.twitter.dto.tweet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class HiddenResponseDTO {

  private HiddenDataDTO data;

  @NoArgsConstructor
  @Setter
  @Getter
  public static class HiddenDataDTO{
      private boolean hidden;
  }

}
