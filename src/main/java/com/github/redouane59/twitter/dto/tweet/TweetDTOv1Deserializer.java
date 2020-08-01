package com.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.redouane59.twitter.TwitterClient;

import java.io.IOException;
import java.util.List;


public class TweetDTOv1Deserializer extends StdDeserializer<TweetDTOv1> {

    public TweetDTOv1Deserializer() {
        this(null);
    }

    public TweetDTOv1Deserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TweetDTOv1 deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return TwitterClient.OBJECT_MAPPER.readValue(node.get("tweet").toString(), TweetDTOv1.class);
    }
}
