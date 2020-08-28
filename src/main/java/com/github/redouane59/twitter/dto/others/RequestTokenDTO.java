package com.github.redouane59.twitter.dto.others;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestTokenDTO {

  @JsonProperty("oauth_token")
  private String oauthToken;
  @JsonProperty("oauth_token_secret")
  private String oauthTokenSecret;
}
