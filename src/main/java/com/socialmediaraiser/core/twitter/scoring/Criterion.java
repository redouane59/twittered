package com.socialmediaraiser.core.twitter.scoring;

import lombok.Getter;

@Getter
public enum Criterion {

    NB_FOLLOWERS,
    NB_FOLLOWINGS,
    RATIO,
    LAST_UPDATE,
    DESCRIPTION,
    LOCATION,
    NB_FAVS,
    NB_TWEETS,
    COMMON_FOLLOWERS;
}
