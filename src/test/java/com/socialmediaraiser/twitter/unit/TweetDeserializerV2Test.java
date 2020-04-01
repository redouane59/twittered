package com.socialmediaraiser.twitter.unit;

import com.socialmediaraiser.twitter.dto.tweet.ContextAnnotation;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv2;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TweetDeserializerV2Test {

    private File tweetFile1 = new File(getClass().getClassLoader().getResource("tests/tweet_example_v2.json").getFile());
    private ITweet tweetv2 = TwitterClient.OBJECT_MAPPER.readValue(tweetFile1, TweetDTOv2.class);

    public TweetDeserializerV2Test() throws IOException {
    }

    @Test
    public void testTweetId(){
        assertEquals("1224044675406925824",tweetv2.getId());
    }

    @Test
    public void testTweetText(){
        assertEquals("@RedouaneBali @TwitterAPI Try to use some function construct of the recebt Java version. It is a good train to improve your procedural code :)", tweetv2.getText());
    }

    @Test
    public void testRetweetCount(){
        assertEquals(0, tweetv2.getRetweetCount());
    }

    @Test
    public void testLikeCount(){
        assertEquals(2, tweetv2.getLikeCount());
    }

    @Test
    public void testReplyCount(){
        assertEquals(2, tweetv2.getReplyCount());
    }

    @Test
    public void testQuoteCount(){
        assertEquals(0, tweetv2.getQuoteCount());
    }

    @Test
    public void testCreateAt(){
        assertEquals(ConverterHelper.getDateFromTwitterDateV2("2020-02-02T18:59:26.000Z"), tweetv2.getCreatedAt());
    }

    @Test
    public void testinReplyToUserId(){
        assertEquals("1120050519182016513", tweetv2.getInReplyToUserId());
    }

    @Test
    public void testinReplyToStatusId(){
        assertEquals("1224041905333379073", tweetv2.getInReplyToStatusId());
    }

    @Test
    public void testLang(){
        assertEquals("en", tweetv2.getLang());
    }

    @Test
    public void testUser(){
        IUser user = tweetv2.getUser();
        assertNotNull(user);
        assertEquals("marcomornati", user.getName());
        assertEquals("9920272", user.getId());
        assertEquals(398, user.getFollowersCount());
        assertEquals(768, user.getFollowingCount());
    }

    @Test
    public void testContextAnnotations(){
        List<ContextAnnotation> contextAnnotationList = tweetv2.getContextAnnotations();
        assertNotNull(contextAnnotationList);
        assertEquals(2, contextAnnotationList.size());
        ContextAnnotation contextAnnotation1 = contextAnnotationList.get(0);
        ContextAnnotation contextAnnotation2 = contextAnnotationList.get(1);
        assertEquals("65", contextAnnotation1.getDomain().getId());
        assertEquals("Interests and Hobbies Vertical", contextAnnotation1.getDomain().getName());
        assertEquals("Top level interests and hobbies groupings, like Food or Travel", contextAnnotation1.getDomain().getDescription());
        assertEquals("848921413196984320", contextAnnotation2.getEntity().getId());
        assertEquals("Computer programming", contextAnnotation2.getEntity().getName());
        assertEquals("Computer programming", contextAnnotation2.getEntity().getDescription());
    }
}
