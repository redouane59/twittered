package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TweetCountsList {

  private List<TweetCountData> data;
  private TweetCountMeta       meta;

  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Getter
  public static class TweetCountData {

    @JsonProperty("tweet_count")
    private int           tweetCount;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime start;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime end;
  }

  public static class LocalDateDeserializer extends StdDeserializer<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    protected LocalDateDeserializer() {
      super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
    throws IOException {
      return ConverterHelper.getDateFromTwitterStringV2(jp.readValueAs(String.class));
    }

  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Getter
  public static class TweetCountMeta {

    @JsonProperty("total_tweet_count")
    private int    totalTweetCount;
    @JsonProperty("next_token")
    private String nextToken;
  }

}
