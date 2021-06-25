package io.github.redouane59.twitter;

import io.github.redouane59.twitter.dto.tweet.Tweet;

public interface IAPIEventListener {

  /**
   * Triggered when a problem from the API occurs on the stream
   */
  void onStreamError(int httpCode, String error);

  /**
   * Triggered when a tweet is received
   */
  void onTweetStreamed(Tweet tweet);

  /**
   * This event happens if we receive something different from a Tweet.class
   */
  void onUnknownDataStreamed(String json);

  /**
   * Triggered on a network issue
   */
  void onStreamEnded(Exception e);

}
