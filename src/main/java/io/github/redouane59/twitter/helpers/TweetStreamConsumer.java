package io.github.redouane59.twitter.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.scribejava.core.model.Response;
import io.github.redouane59.twitter.IAPIEventListener;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * Consumes the data from the buffers received from twitter
 */
@Slf4j
public class TweetStreamConsumer {

  private StringBuilder buffer = new StringBuilder();

  /**
   * Consumes the data from the buffers received from twitter A tweet can be sent in multiple chunks, or having in one chunk multiple tweet.
   *
   * @return true if at least one tweet is complete, false otherwise
   */
  public boolean consumeBuffer(String data) {

    // Ignoring Heartbeat or empty line.
    if (data.trim().isEmpty()) {
      return false;
    }

    // Check if the buffer is empty and we receive a valid json data
    if (buffer.toString().isEmpty() && (!data.trim().startsWith("{"))) {
      LOGGER.warn("Invalid JSON Start Character. Ignoring : " + data);
      return false;
    }
    buffer.append(data);

    // If we detect a \r\n in the buffer, then at least a tweet is complete
    return (buffer.indexOf("\r\n") != -1);
  }

  /**
   * Consumes a stream. As we read the data based on \r\n , we don't expect having a partial tweet so, we don't use internal StringBuilder to rebuild
   * a tweet.
   */
  public <T> void consumeStream(IAPIEventListener listener, final Response response, final Class<? extends T> clazz) {
    if (listener == null) {
      throw new IllegalAccessError("Missing listener");
    }

    // Make Use of a Thread as the reader is blocking
    new Thread(() -> {
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(response.getStream(), StandardCharsets.UTF_8))) {
        boolean bValid = true;
        while (bValid) {
          bValid = readSocket(listener, response, clazz, reader);
        }
      } catch (IOException e) {
        listener.onStreamEnded(e);
      }
    }).start();

  }

  /**
   * Reads the data from the socket. If the socket has a readtimeout, it'll be handled and continue to listen to the socket. Other IOException will be
   * thrown and the read will be stopped.
   */
  private <T> boolean readSocket(IAPIEventListener listener, final Response response,
                                 final Class<? extends T> clazz, BufferedReader reader)
  throws IOException {
    String line;
    try {
      line = reader.readLine();
      if (line == null) {
        return false;
      }
      // Avoid empty line (heartbeat)
      if (!line.trim().isEmpty() && (!handleData(listener, response, clazz, line))) {
        return false;
      }
    } catch (SocketTimeoutException e) {
      // Nothing to do
    }
    return true;
  }

  /**
   * Returns true if the data received are not in error depending on the response.code
   */
  private <T> boolean handleData(IAPIEventListener listener, final Response response, final Class<? extends T> clazz, String line) {
    if (response.getCode() == 200) {
      if (clazz == TweetV2.class) {
        try {
          listener.onTweetStreamed((TweetV2) TwitterClient.OBJECT_MAPPER.readValue(line, clazz));
        } catch (JsonProcessingException e) {
          listener.onUnknownDataStreamed(line);
        }
      } else {
        listener.onUnknownDataStreamed(line);
      }
      return true;
    } else {
      listener.onStreamError(response.getCode(), line);
      return false;
    }
  }


  /**
   * Check if the string supplied is valid
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
   */
  public String[] getJsonTweets() {
    List<String> result = Stream.of(buffer.toString().split("\n")).filter(s -> !s.trim().isEmpty())
                                .collect(Collectors.toList());
    if (result.isEmpty()) {
      return new String[0];
    }

    // Check if the last result is complete...
    String  lastJSON = result.get(result.size() - 1);
    boolean complete = isValidJSON(lastJSON);
    // Reinit the StringBuilder...
    buffer = new StringBuilder();
    // If not complete, reconsume the last buffer
    if (!complete) {
      consumeBuffer(lastJSON);
      result.remove(result.size() - 1);
    }
    return result.stream().toArray(String[]::new);
  }

}
