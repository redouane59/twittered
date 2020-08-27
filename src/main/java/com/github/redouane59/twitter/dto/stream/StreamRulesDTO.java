package com.github.redouane59.twitter.dto.stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
  @Builder
  @Getter
  @Setter
  public static class StreamRule {
    @JsonInclude(Include.NON_NULL)
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
    private StreamSummary summary;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class StreamSummary {
      private int deleted;
      @JsonProperty("not_deleted")
      private int notDeleted;
    }
  }


}
