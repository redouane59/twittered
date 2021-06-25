package com.github.redouane59.twitter.signature;

import com.github.scribejava.core.model.OAuth1AccessToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwitterCredentials {

  private String apiKey;
  private String apiSecretKey;
  private String accessToken;
  private String accessTokenSecret;
  
  public OAuth1AccessToken asAccessToken() {
	  return new OAuth1AccessToken(getAccessToken(), getAccessTokenSecret());
  }
}
