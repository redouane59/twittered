package com.github.redouane59.twitter;

import com.github.redouane59.twitter.dto.tweet.TweetV2;

public interface IAPIEventListener {

  /**
   * Triggered where an error from an API is received an httpcode different from
   * 200 is considered as an error.
   * 
   * @param httpCode
   * @param json
   */
  void onError(int httpCode, String error);

  /**
   * Triggered when a problem from the API occurs on the stream
   * 
   * @param httpCode
   * @param json
   */
  void onStreamError(int httpCode, String error);

  /**
   * Triggered when a tweet is received
   * 
   * @param tweet
   */
  void onTweetStreamed(TweetV2 tweet);

  /**
   * This event happens if we receive something different from a Tweet.class
   * 
   * @param json
   */
  void onUnknownDataStreamed(String json);

  /**
   * Triggered on a network issue
   */
  void onStreamEnded(Exception e);

}
