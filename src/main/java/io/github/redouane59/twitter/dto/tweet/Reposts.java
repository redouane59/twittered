package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.dto.tweet.AdditionalProperties;
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
public class Reposts {

  @JsonProperty("media_keys")
  private int count;
  private AdditionalProperties additionalProperties;

}
