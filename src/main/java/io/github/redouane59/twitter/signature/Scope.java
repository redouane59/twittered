package io.github.redouane59.twitter.signature;

import lombok.Getter;

public enum Scope {

  TWEET_READ("tweet.read"),
  TWEET_WRITE("tweet.write"),
  TWEET_MODERATE_WRITE("tweet.moderate.write"),
  USERS_READ("users.read"),
  FOLLOWS_READ("follows.read"),
  FOLLOWS_WRITE("follows.write"),
  OFFLINE_ACCESS("offline.access"),
  SPACE_READ("space.read"),
  MUTE_READ("mute.read"),
  MUTE_WRITE("mute.write"),
  LIKE_READ("like.read"),
  LIKE_WRITE("like.write"),
  LIST_READ("list.read"),
  LIST_WRITE("list.write"),
  BLOCK_READ("block.read"),
  BLOCK_WRITE("block.write");

  @Getter
  private String name;

  Scope(String name) {
    this.name = name;
  }

  public static Scope getValue(String value) {
    for (Scope e : Scope.values()) {
      if (e.name.equals(value)) {
        return e;
      }
    }
    return null;// not found
  }
}
