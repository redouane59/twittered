package com.github.redouane59.twitter.dto.stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta.StreamMetaBuilder;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta.StreamSummary.StreamSummaryBuilder;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamRule.StreamRuleBuilder;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamRulesBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = StreamRulesBuilder.class)
public class StreamRules {

  private List<StreamRule> data;
  private StreamMeta       meta;

  @lombok.Value
  @lombok.Builder
  @JsonDeserialize(builder = StreamRuleBuilder.class)
  public static class StreamRule {

    private String id;
    private String value;
    private String tag;

    @JsonPOJOBuilder(withPrefix = "")
    public static class StreamRuleBuilder {

    }
  }

  @lombok.Value
  @lombok.Builder
  @JsonDeserialize(builder = StreamMetaBuilder.class)
  public static class StreamMeta {

    private String        sent;
    private StreamSummary summary;

    @lombok.Value
    @lombok.Builder
    @JsonDeserialize(builder = StreamSummaryBuilder.class)
    public static class StreamSummary {

      private int deleted;
      @JsonProperty("not_deleted")
      private int notDeleted;

      @JsonPOJOBuilder(withPrefix = "")
      public static class StreamSummaryBuilder {

      }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class StreamMetaBuilder {

    }
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class StreamRulesBuilder {

  }

}
