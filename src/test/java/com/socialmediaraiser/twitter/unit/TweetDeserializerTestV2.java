package com.socialmediaraiser.twitter.unit;

import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv2;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TweetDeserializerTestV2 {

    private File tweetFile1 = new File(getClass().getClassLoader().getResource("tests/tweet_example_v2.json").getFile());
    private ITweet tweetv2 = TwitterClient.OBJECT_MAPPER.readValue(tweetFile1, TweetDTOv2.class);

    public TweetDeserializerTestV2() throws IOException {
    }

    @Test
    public void testTweetId(){
        assertEquals("1224041905333379073",tweetv2.getId());
    }

    @Test
    public void testTweetText(){
        assertEquals("If some JAVA developers want to play with @TwitterAPI , I'm developing a library to consume their different endpoints including the most recent ones from Labs. Any feedback welcome\uD83D\uDE01\n\uD83D\uDC49 https://t.co/20S8ScRohV \uD83D\uDC48\n#java #twitter #api #twitterapi #dev #backend #softwaredevelopment", tweetv2.getText());
    }

    @Test
    public void testRetweetCount(){
        assertEquals(2, tweetv2.getRetweetCount());
    }

    @Test
    public void testLikeCount(){
        assertEquals(5, tweetv2.getLikeCount());
    }

    @Test
    public void testReplyCount(){
        assertEquals(4, tweetv2.getReplyCount());
    }

    @Test
    public void testCreateAt(){
        assertEquals(ConverterHelper.getDateFromTwitterDateV2("2020-02-02T18:48:26.000Z"), tweetv2.getCreatedAt());
    }
/*
    @Test
    public void testinReplyToUserId(){
        assertEquals("92073489", tweetv2.getInReplyToUserId());
    }

    @Test
    public void testinReplyToStatusId(){
        assertEquals("1222130381815795717", tweetv2.getInReplyToStatusId());
    }
*/
    @Test
    public void testLang(){
        assertEquals("en", tweetv2.getLang());
    }

/*    @Test
    public void testUser(){
        IUser user = tweetv2.getUser();
        assertNotNull(user);
        assertEquals("RedTheOne", user.getName());
        assertEquals("92073489", user.getId());
        assertEquals(5929, user.getFollowersCount());
        assertEquals(2587, user.getFollowingCount());
    } */
}
