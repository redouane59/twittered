package io.github.redouane59.twitter.dto.tweet.entities;

public interface UrlEntity extends BaseEntity {

  String getUrl();

  String getDisplayUrl();

  String getExpandedUrl();

  int getStatus();

  String getDescription();

  String getTitle();

  String getUnwoundedUrl();
}
