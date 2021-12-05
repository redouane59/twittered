package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.TweetList.TweetMeta;
import io.github.redouane59.twitter.dto.tweet.TweetV2.Includes;
import io.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;
import io.github.redouane59.twitter.dto.user.UserV2.UserData;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TweetListDeserializer extends StdDeserializer<TweetList> {

  protected TweetListDeserializer() {
    super(TweetListDeserializer.class);
  }

  @Override
  public TweetList deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
    TweetList result = TweetList.builder().build();
    JsonNode  node   = jsonParser.getCodec().readTree(jsonParser);
    if (node.has("meta")) {
      result.setMeta(TwitterClient.OBJECT_MAPPER.readValue(node.get("meta").toString(), TweetMeta.class));
    }
    if (node.has("data")) {
      List<TweetData>
          list =
          TwitterClient.OBJECT_MAPPER.readValue(node.get("data").toString(),
                                                TwitterClient.OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, TweetData.class));
      if (node.has("includes")) {
        result.setIncludes(TwitterClient.OBJECT_MAPPER.readValue(node.get("includes").toString(), Includes.class));
        // in order to enrich the TweetData object (from data field) adding the User object (instead of just the author_id)
        for (TweetData tweetData : list) {
          Optional<UserData> matchingUser = result.getIncludes().getUsers().stream().filter(p -> p.getId().equals(tweetData.getAuthorId())).
                                                  findFirst();
          matchingUser.ifPresent(tweetData::setUser);
        }
      }
      result.setData(list);
    }
    return result;
  }
}
