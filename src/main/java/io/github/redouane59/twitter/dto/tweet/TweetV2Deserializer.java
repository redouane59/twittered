package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.helpers.JsonHelper;
import java.io.IOException;


public class TweetV2Deserializer extends StdDeserializer<TweetV2> {

  public TweetV2Deserializer() {
    this(null);
  }

  public TweetV2Deserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public TweetV2 deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    return JsonHelper.fromJson(node.get("tweet"), TweetV2.class);
  }
}
