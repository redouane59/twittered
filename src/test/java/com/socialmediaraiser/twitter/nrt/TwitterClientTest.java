package com.socialmediaraiser.twitter.nrt;

import com.socialmediaraiser.RelationType;
import com.socialmediaraiser.twitter.IUser;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.helpers.RequestHelper;
import com.socialmediaraiser.twitter.dto.others.RequestTokenDTO;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDataDTO;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TwitterClientTest {

    private TwitterClient twitterClient = new TwitterClient();

    @Test
    public void testGetFollowingIdsById() {
        List<String> followings = twitterClient.getFollowingIds("882266619115864066");
        assertTrue(followings.size() > 200);
    }

    @Test
    public void testGetFollowersIdsById() {
        List<String> followers = twitterClient.getFollowerIds("882266619115864066");
        assertTrue(followers.size() > 200);
    }

    @Test
    public void testGetFollowersUsersById() {
        List<IUser> followers = twitterClient.getFollowerUsers("882266619115864066");
        assertTrue(followers.size() > 200);
    }

    @Test
    public void testFriendshipByIdYes() {
        String userId1 = "92073489";
        String userId2 = "723996356";
        RelationType result = twitterClient.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void getUserByUserName() {
        String userName = "RedTheOne";
        IUser result = twitterClient.getUserFromUserName(userName);
        assertEquals("92073489", result.getId());
        userName = "RedouaneBali";
        result = twitterClient.getUserFromUserName(userName);
        assertEquals("RedouaneBali", result.getName());
    }

    @Test
    public void testFriendshipByIdNo() {
        String userId1 = "92073489";
        String userId2 = "1976143068";
        RelationType result = twitterClient.getRelationType(userId1, userId2);
        assertNotEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void testGetUserInfoName() {
        String userId = "92073489";
        IUser user = twitterClient.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getName());
    }

    @Test
    public void testGetUserInfoId() {
        String userId = "92073489";
        IUser user = twitterClient.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
    }

    @Test
    public void testGetUserInfoFavouritesDateOfCreation() {
        String userId = "92073489";
        IUser user = twitterClient.getUserFromUserId(userId);
        assertTrue(user.getDateOfCreation() != null);
    }

    @Test
    public void testGetUserInfoStatusesCount() {
        String userId = "92073489";
        IUser user = twitterClient.getUserFromUserId(userId);
        assertTrue(user.getTweetCount() > 0);
    }

/* // @todo to add then
    @Test
    public void testGetUserInfoLastUpdate() {
        String userId = "92073489";
        IUser user = twitterClient.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
        assertTrue(user.getLastUpdate() != null);
    }
 */
    /* // @todo to add then
    @Test
    public void testGetUserInfoFollowingRatio() {
        String userId = "92073489";
        IUser user = twitterClient.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
        assertTrue(user.getFollowersRatio() > 1);
    } */

    @Test
    public void testGetUserWithCache() {
        String userId = "92073489";
        IUser user = twitterClient.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getName());
        user = twitterClient.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getName());
    }

    @Test
    public void testGetUsersFromUserIds() {
        List<String> ids = new ArrayList<>();
        ids.add("92073489"); // RedTheOne
        ids.add("22848599"); // Soltana
        List<IUser> result = twitterClient.getUsersFromUserIds(ids);
        assertEquals("RedTheOne", result.get(0).getName());
        assertEquals("Soltana", result.get(1).getName());
    }

    @Test
    public void testGetRateLimitStatus() {
        assertNotEquals(null, twitterClient.getRateLimitStatus());
    }

    @Test
    public void testRelationBetweenUsersIdFriends() {
        String userId1 = "92073489";
        String userId2 = "723996356";
        RelationType result = twitterClient.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void testRelationBetweenUsersIdNone() {
        String userId1 = "92073489";
        String userId2 = "1976143068";
        RelationType result = twitterClient.getRelationType(userId1, userId2);
        assertEquals(RelationType.NONE, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollowing() {
        String userId1 = "92073489";
        String userId2 = "126267113";
        RelationType result = twitterClient.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWING, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollower() {
        String userId1 = "92073489";
        String userId2 = "1218125226095054848";
        RelationType result = twitterClient.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWER, result);
    }

    @Test
    public void testGetRetweetersId() {
        String tweetId = "1078358350000205824";
        assertTrue(twitterClient.getRetweetersId(tweetId).size() > 10);
    }

    // @todo to add then

    @Test
    public void getLastUpdate() {
        String userId = "92073489";
        IUser user = twitterClient.getUserFromUserId(userId);
        Date now = new Date();
        Date lastUpdate = user.getLastUpdate();
        long diffDays = (now.getTime() - lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
        assertTrue(diffDays < 15);
    }

    /* //@todo to add
    @Test
    public void getMostRecentTweets(){
        String userId = "92073489";
        IUser user = twitterClient.getUserFromUserId(userId);
        assertFalse(user.getMostRecentTweet().isEmpty());
    } */


    @Test
    public void testGetLastTweetByUserName() {
        String userName = "RedTheOne";
        List<ITweet> response = twitterClient.getUserLastTweets(userName, 2);
        assertTrue(response.get(0).getLang().equals("fr")
                || response.get(1).getLang().equals("fr"));
    }

    @Test
    public void testGetLastTweetByUserId() {
        String userId = "92073489";
        List<ITweet> response = twitterClient.getUserLastTweets(userId, 3);
        assertTrue(response.get(0).getLang().equals("fr")
                || response.get(1).getLang().equals("fr"));
    }

  /*   @Test
    @Disabled
   public void testSearchForTweetsFull() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmm");
        String strdate1 = "201901300000";
        String strdate2 = "202001310000";
        List<Tweet> results = null;
        results = twitterClient.searchForTweets("@redtheone @demi_sword", 10, strdate1, strdate2,
                this.twitterClient.getUrlHelper().getSearchTweetsUrlFull());
        assertNotNull(results);
        assertTrue(results.size() > 0);
    }
*/
  /*
    @Test
    @Disabled
    public void testSearchForTweets() {
        List<Tweet> results = twitterClient.searchForLast100Tweets30days("@RedTheOne -RT",
                ConverterHelper.getStringFromDate(ConverterHelper.minutesBefore(75)));
        assertNotNull(results);
        assertTrue(results.size() > 0);
    } */

    @Test
    public void testGetTokens(){
        RequestHelper.TWITTER_CREDENTIALS.setAccessToken("");
        RequestHelper.TWITTER_CREDENTIALS.setAccessTokenSecret("");
        RequestTokenDTO result = twitterClient.getRequestHelper().executeTokenRequest();
        assertTrue(result.getOauthToken().length()>1);
        assertTrue(result.getOauthTokenSecret().length()>1);
    }

    @Test
    public void testGetTweetDataFile() throws IOException {
        File file = new File(this.getClass().getClassLoader().getResource("tweet.json").getFile());
        List<TweetDataDTO> result = twitterClient.readTwitterDataFile(file);
        assertTrue(result.size()>10);
        assertNotNull(result.get(0).getTweet().getCreatedAt());
        assertNotNull(result.get(0).getTweet().getId());
        assertNotNull(result.get(0).getTweet().getText());
        assertNotNull(result.get(0).getTweet().getInReplyToUserId());
    }
}