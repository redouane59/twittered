package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadMediaResponse {

  @JsonProperty("media_id_string")
  private String mediaId;
  @JsonProperty("expires_after_secs")
  private int    expiresAfterSecs;
  private int    size;
  @JsonProperty("media_key")
  private String mediaKey;

}
