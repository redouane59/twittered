package com.socialmediaraiser.twitter.unit;

import com.socialmediaraiser.twitter.helpers.JsonHelper;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TweetDeserializerTestV1 {

    private File tweetFile1 = new File(getClass().getClassLoader().getResource("tests/tweet_example_v1.json").getFile());
    private ITweet tweetV1 = JsonHelper.OBJECT_MAPPER.readValue(tweetFile1, TweetDTOv1.class);

    public TweetDeserializerTestV1() throws IOException {
    }

    @Test
    public void testTweetId(){
        assertEquals("1223664533451026434",tweetV1.getId());
    }

    @Test
    public void testTweetText(){
        assertEquals("@RedTheOne Tu t'es es sorti", tweetV1.getText());
    }

    @Test
    public void testRetweetCount(){
        assertEquals(2, tweetV1.getRetweetCount());
    }

    @Test
    public void testFavoriteCount(){
        assertEquals(1, tweetV1.getLikeCount());
    }

    @Test
    public void testReplyCount(){
        assertEquals(3, tweetV1.getReplyCount());
    }

    @Test
    public void testCreateAt(){
        assertEquals(JsonHelper.getDateFromTwitterString("Sat Feb 01 17:48:54 +0000 2020"), tweetV1.getCreatedAt());
    }

    @Test
    public void testinReplyToUserId(){
        assertEquals("92073489", tweetV1.getInReplyToUserId());
    }

    @Test
    public void testinReplyToStatusId(){
        assertEquals("1223576831170879489", tweetV1.getInReplyToStatusId());
    }

    @Test
    public void testLang(){
        assertEquals("fr", tweetV1.getLang());
    }

    @Test
    public void testUser(){
        assertNotNull(tweetV1.getUser());
        assertEquals("723996356", tweetV1.getUser().getId());
        assertEquals("naiim75012", tweetV1.getUser().getName());
        assertEquals("France", tweetV1.getUser().getLocation());
        assertEquals(6339, tweetV1.getUser().getFollowersCount());
        assertEquals(392, tweetV1.getUser().getFollowingCount());
        assertEquals(83425, tweetV1.getUser().getTweetCount());
        assertEquals(JsonHelper.getDateFromTwitterString("Sun Jul 29 13:24:02 +0000 2012"), tweetV1.getUser().getDateOfCreation());
    }
}
