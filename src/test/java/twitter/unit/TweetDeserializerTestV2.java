package twitter.unit;

import com.socialmediaraiser.core.twitter.IUser;
import com.socialmediaraiser.core.twitter.helpers.JsonHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.tweet.ITweet;
import com.socialmediaraiser.core.twitter.helpers.dto.tweet.TweetDTOv2;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TweetDeserializerTestV2 {

    private File tweetFile1 = new File(getClass().getClassLoader().getResource("tests/tweet_example_v2.json").getFile());
    private ITweet tweetv2 = JsonHelper.OBJECT_MAPPER.readValue(tweetFile1, TweetDTOv2.class);

    public TweetDeserializerTestV2() throws IOException {
    }

    @Test
    public void testTweetId(){
        assertEquals("1222203395647397889",tweetv2.getId());
    }

    @Test
    public void testTweetText(){
        assertEquals("@RedTheOne Mais c’est trop bon le Perrier", tweetv2.getText());
    }

    @Test
    public void testRetweetCount(){
        assertEquals(1, tweetv2.getRetweetCount());
    }

    @Test
    public void testFavoriteCount(){
        assertEquals(5, tweetv2.getLikeCount());
    }

    @Test
    public void testReplyCount(){
        assertEquals(1, tweetv2.getReplyCount());
    }

    @Test
    public void testCreateAt(){
        assertEquals(JsonHelper.getDateFromTwitterDateV2("2020-01-28T17:02:51.000Z"), tweetv2.getCreatedAt());
    }

    @Test
    public void testinReplyToUserId(){
        assertEquals("92073489", tweetv2.getInReplyToUserId());
    }

    @Test
    public void testinReplyToStatusId(){
        assertEquals("1222130381815795717", tweetv2.getInReplyToStatusId());
    }

    @Test
    public void testLang(){
        assertEquals("fr", tweetv2.getLang());
    }

    @Test
    public void testUser(){
        IUser user = tweetv2.getUser();
        assertNotNull(user);
        assertEquals("RedTheOne", user.getName());
        assertEquals("92073489", user.getId());
        assertEquals(5929, user.getFollowersCount());
        assertEquals(2587, user.getFollowingCount());
    }
}
