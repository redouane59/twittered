package com.socialmediaraiser.twitter.dto.others;

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
    @JsonProperty("auth_callback_confirmed")
    private boolean authCallbackConfirmed;
}
