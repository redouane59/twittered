package com.socialmediaraiser.twitter.dto.others;

import lombok.Data;

@Data
public class RequestTokenDTO {
    private String oauthToken;
    private String oauthTokenSecret;
}
