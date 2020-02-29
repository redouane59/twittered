package com.socialmediaraiser.twitter.unit;

import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitter.dto.user.UserDTOv2;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserDeserializerV2Test {

    private  File userFile2 = new File(getClass().getClassLoader().getResource("tests/user_example_v2.json").getFile());
    private IUser userV2 = TwitterClient.OBJECT_MAPPER.readValue(userFile2, UserDTOv2.class);

    public UserDeserializerV2Test() throws IOException {
    }

    @Test
    public void testGetUserIdV2() {
        assertEquals("92073489", userV2.getId());
    }

    @Test
    public void testGetUserScreenNameV2() {
        assertEquals("RedTheOne", userV2.getName());
    }

    @Test
    public void testGetUserLocationV2() {
        assertEquals("Madrid, Spain", userV2.getLocation());
    }

    @Test
    public void testGetUserDescriptionV2() {
        assertEquals("En vérité, j'suis à ma place quand je les dérange. Jamais dans la tendance, toujours dans la bonne direction... #Lille #Montréal #Madrid \uD83D\uDC8D\uD83C\uDDE9\uD83C\uDDFF\uD83C\uDDEE\uD83C\uDDF9", userV2.getDescription());
    }

    @Test
    public void testGetUserFollowersCount() {
        assertEquals(5708, userV2.getFollowersCount());
    }

    @Test
    public void testGetUserFollowingCount() {
        assertEquals(2357, userV2.getFollowingCount());
    }

    @Test
    public void testGetUserTweetsCount() {
        assertEquals(38793, userV2.getTweetCount());
    }

    @Test
    public void testGetUserDateOfCreation() {
        assertEquals(ConverterHelper.getDateFromTwitterDateV2("2009-11-23T17:53:15.000Z"), userV2.getDateOfCreation());
    }

    @Test
    public void testGetUserPinnedTweet(){
        ITweet pinnedTweet = userV2.getPinnedTweet();
        assertNotNull(pinnedTweet);
        assertEquals("92073489", pinnedTweet.getAuthorId());
        assertEquals(ConverterHelper.getDateFromTwitterDateV2("2018-08-30T15:50:15.000Z"), pinnedTweet.getCreatedAt());
        assertEquals("1035192987008020480", pinnedTweet.getId());
        assertEquals("fr", pinnedTweet.getLang());
        assertEquals(1910, pinnedTweet.getRetweetCount());
        assertEquals(232, pinnedTweet.getReplyCount());
        assertEquals(2286, pinnedTweet.getLikeCount());
        assertEquals(237, pinnedTweet.getQuoteCount());
        assertTrue(pinnedTweet.getText().contains("Thread sur ce qui m'a poussé à partir"));
    }

}



