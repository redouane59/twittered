package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.redouane59.twitter.TwitterClient;
import java.io.IOException;


public class TweetV1Deserializer extends StdDeserializer<TweetV1> {

  public TweetV1Deserializer() {
    this(null);
  }

  public TweetV1Deserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public TweetV1 deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    return TwitterClient.OBJECT_MAPPER.readValue(node.get("tweet").toString(), TweetV1.class);
  }
}
