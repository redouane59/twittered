package com.github.redouane59.twitter.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.TweetV2;
import com.github.scribejava.core.model.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumes the data from the buffers received from twitter
 * 
 */
@Slf4j
public class TweetStreamConsumer {

  private StringBuilder buffer;
  private AbstractRequestHelper helper;
  public TweetStreamConsumer(AbstractRequestHelper helper) {
    this.buffer = new StringBuilder();
    this.helper = helper;
  }

  /**
   * Consumes the data from the buffers received from twitter A tweet can be sent
   * in multiple chunks, or having in one chunk multiple tweet.
   * 
   * @param data
   * @return true if at least one tweet is complete, false otherwise
   */
  public boolean consumeBuffer(String data) {

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
   * Consumes a stream.
   * As we read the data based on \r\n , we don't expect having a partial tweet
   * so, we don't use internal StringBuilder to rebuild a tweet.
   * @param response
   */
  public <T> void consumeStream(final Response response, final Class<? extends T> clazz) {
    if (helper.listener == null) throw new IllegalAccessError("Missing listener");

    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream(), StandardCharsets.UTF_8));
    while ( true ) {
      try {
        String s = reader.readLine();
        if (response.getCode() == 200) {
              if (clazz == TweetV2.class) {
                helper.listener.onTweetStreamed( (TweetV2) TwitterClient.OBJECT_MAPPER.readValue(s, clazz) );
              } else {
                helper.listener.onUnknownDataStreamed( s );
              }
        } else {
          helper.notifyError(response.getCode(), s );
          break;
        }
      } catch(IOException e) {
        helper.listener.onStreamEnded( e );
      }
      
    }
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
      this.consumeBuffer(lastJSON);
      result.remove(result.size() - 1);
    }
    return result.stream().toArray(String[]::new);
  }

}
