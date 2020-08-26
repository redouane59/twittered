package com.github.redouane59.twitter.dto.stream;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StreamRulesDTO {

  private List<StreamRule> data;
  private StreamMeta       meta;

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class StreamRule {
      private String id;
      private String value;
      private String tag;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class StreamMeta {
    private String sent;
  }


}
