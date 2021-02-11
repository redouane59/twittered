package com.github.redouane59.twitter.helpers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.redouane59.twitter.TwitterClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumes the data from the buffers received from twitter
 * 
 */
@Slf4j
public class TweetStreamConsumer {

  private StringBuilder buffer;

  public TweetStreamConsumer() {
    this.buffer = new StringBuilder();
  }

  /**
   * Consumes the data from the buffers received from twitter A tweet can be sent
   * in multiple chunks, or having in one chunk multiple tweet.
   * 
   * @param data
   * @return true if at least one tweet is complete, false otherwise
   */
  public boolean consume(String data) {

    // Ignoring Heartbeat or empty line.
    if (data.trim().isEmpty()) return false;

    // Check if the buffer is empty and we receive a valid json data
    if (this.buffer.isEmpty() && (!data.trim().startsWith("{"))) {
      LOGGER.warn("Invalid JSON Start Character. Ignoring : " + data);
      return false;
    }
    buffer.append(data);

    // If we detect a \r\n in the buffer, then at least a tweet is complete
    return (this.buffer.indexOf("\r\n") != -1);
  }

  /**
   * Check if the string supplied is valid
   * 
   * @param json
   * @return
   */
  private boolean isValidJSON(String json) {
    try {
      TwitterClient.OBJECT_MAPPER.readTree(json);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Returns an array of string containing 0 to n tweets
   * 
   * @return
   */
  public String[] getJsonTweets() {
    List<String> result = Stream.of(buffer.toString().split("\n")).filter(s -> !s.trim().isEmpty())
        .collect(Collectors.toList());
    if (result.isEmpty())
      return new String[0];

    // Check if the last result is complete...
    String lastJSON = result.get(result.size() - 1);
    boolean complete = isValidJSON(lastJSON);
    // Reinit the StringBuilder...
    this.buffer = new StringBuilder();
    // If not complete, reconsume the last buffer
    if (!complete) {
      this.consume(lastJSON);
      result.remove(result.size() - 1);
    }
    return result.stream().toArray(String[]::new);
  }

}
