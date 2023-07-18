package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadMediaProcessingInfo {

  @JsonProperty("state")
  private String state;
  @JsonProperty("check_after_secs")
  private int    checkAfterSecs;
  @JsonProperty("progress_percent")
  private int    progressPercent;
  @JsonProperty("media_key")
  private String mediaKey;
  private UploadMediaProcessingError error;

}
