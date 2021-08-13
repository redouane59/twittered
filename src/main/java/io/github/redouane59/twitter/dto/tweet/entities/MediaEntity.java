package io.github.redouane59.twitter.dto.tweet.entities;

public interface MediaEntity extends BaseEntity {

  String getDisplayUrl();

  String getExpandedUrl();

  String getMediaUrl();

  String getType();

  String getUrl();

  long getId();
}
