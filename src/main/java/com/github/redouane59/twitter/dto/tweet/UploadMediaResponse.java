package com.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class UploadMediaResponse {

  @JsonProperty("media_id")
  private long mediaId;
  private long size;

}
