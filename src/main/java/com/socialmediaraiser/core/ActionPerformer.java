package com.socialmediaraiser.core;

public interface ActionPerformer {

    boolean follow(String userId);
    boolean unfollow(String userId);

}
