package io.github.redouane59.twitter.dto.collections;

/**
 * timeline_order used when creating a collection - https://api.twitter.com/1.1/collections/create.json
 */
public enum TimeLineOrder {

  // oldest tweet first
  CHRONOLOGICAL("tweet_chron"),
  // order tweets are added to the collection (default)
  CURATION_ORDER("curation_reverse_chron"),
  // most recent tweet first
  CHRONOLOGICAL_REVERSE("tweet_reverse_chron");

  private final String value;

  TimeLineOrder(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

}
