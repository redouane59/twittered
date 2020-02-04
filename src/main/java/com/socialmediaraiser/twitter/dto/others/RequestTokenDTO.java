package com.socialmediaraiser.twitter.dto.others;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTokenDTO {
    private String oauthToken;
    private String oauthTokenSecret;
}
