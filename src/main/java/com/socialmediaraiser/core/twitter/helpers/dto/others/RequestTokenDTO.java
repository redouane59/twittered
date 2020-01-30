package com.socialmediaraiser.core.twitter.helpers.dto.others;

import lombok.Data;

@Data
public class RequestTokenDTO {
    private String oauthToken;
    private String oauthTokenSecret;
}
