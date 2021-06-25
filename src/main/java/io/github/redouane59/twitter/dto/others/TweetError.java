package io.github.redouane59.twitter.dto.others;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TweetError {

  @JsonProperty("title")
  private String title;

  @JsonProperty("detail")
  private String detail;

  @JsonProperty("connection_issue")
  private String connectionIssue;

  @JsonProperty("type")
  private String type;
}
