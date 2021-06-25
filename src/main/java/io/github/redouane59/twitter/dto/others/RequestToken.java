package io.github.redouane59.twitter.dto.others;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class RequestToken {

  @JsonProperty("oauth_token")
  private String oauthToken;
  @JsonProperty("oauth_token_secret")
  private String oauthTokenSecret;

  public RequestToken(String stringResponse) {
    List<NameValuePair> responseParams = null;
    try {
      responseParams = URLEncodedUtils.parse(new URI("twitter.com?" + stringResponse), StandardCharsets.UTF_8.name());
    } catch (URISyntaxException e) {
      LOGGER.error(e.getMessage(), e);
    }
    for (NameValuePair param : responseParams) {
      if (param.getName().equals("oauth_token")) {
        this.oauthToken = param.getValue();
      } else if (param.getName().equals("oauth_token_secret")) {
        this.oauthTokenSecret = param.getValue();
      }
    }
  }
}
