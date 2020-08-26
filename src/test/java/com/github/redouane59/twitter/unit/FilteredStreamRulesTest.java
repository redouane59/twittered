package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.stream.StreamRulesDTO;
import com.github.redouane59.twitter.dto.tweet.ContextAnnotation;
import com.github.redouane59.twitter.dto.tweet.ITweet;
import com.github.redouane59.twitter.dto.tweet.TweetDTOv2;
import com.github.redouane59.twitter.dto.user.IUser;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FilteredStreamRulesTest {

    private File   rulesFile = new File(getClass().getClassLoader().getResource("tests/stream_rules_example.json").getFile());
    private StreamRulesDTO rules     = TwitterClient.OBJECT_MAPPER.readValue(rulesFile, StreamRulesDTO.class);

    public FilteredStreamRulesTest() throws IOException {
    }

    @Test
    public void testSize(){
        assertEquals(2, rules.getData().size());
    }

    @Test
    public void testIds(){
        assertEquals("1298705840506118146", rules.getData().get(0).getId());
        assertEquals("1298706201161728002", rules.getData().get(1).getId());
    }

    @Test
    public void testValues(){
        assertEquals("algerie", rules.getData().get(0).getValue());
        assertEquals("super", rules.getData().get(1).getValue());
    }

    @Test
    public void testTags(){
        assertEquals("1", rules.getData().get(0).getTag());
        assertEquals("2", rules.getData().get(1).getTag());
    }

    @Test
    public void testMeta(){
        assertEquals("2020-08-26T20:12:54.519Z", rules.getMeta().getSent());
    }
}
