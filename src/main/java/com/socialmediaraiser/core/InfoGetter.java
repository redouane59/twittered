package com.socialmediaraiser.core;

import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;

import java.util.List;

public interface InfoGetter {

  RelationType getRelationType(String userId1, String userId2);

  List<String> getFollowerIds(String userId);

  List<String> getFollowingIds(String userId);

  AbstractUser getUserFromUserName(String userName);
  AbstractUser getUserFromUserId(String userId);

}
