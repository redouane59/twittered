package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class Attachments {

  @JsonProperty("media_keys")
  private List<String> mediaKeys;

  @JsonProperty("media_source_tweet_id")
  private List<String> mediaSourceTweetId;

}
