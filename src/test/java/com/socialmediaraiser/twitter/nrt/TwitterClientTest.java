package com.socialmediaraiser.twitter.nrt;

import com.socialmediaraiser.RelationType;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.dto.others.RequestTokenDTO;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.helpers.AbstractRequestHelper;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
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

    @Test
    public void testGetTweetById(){
        String tweetId = "1224041905333379073";
        ITweet tweet = twitterClient.getTweet(tweetId);
        assertNotNull(tweet);
    }

    @Test
    public void testGetOauth1Token(){
        AbstractRequestHelper.TWITTER_CREDENTIALS.setAccessToken("");
        AbstractRequestHelper.TWITTER_CREDENTIALS.setAccessTokenSecret("");
        RequestTokenDTO result = twitterClient.getOauth1Token();
        assertTrue(result.getOauthToken().length()>1);
        assertTrue(result.getOauthTokenSecret().length()>1);
    }

    @Test
    public void testGetTweetDataFile() throws IOException {
        File file = new File(this.getClass().getClassLoader().getResource("tweet.json").getFile());
        List<TweetDTOv1> result = twitterClient.readTwitterDataFile(file);
        assertTrue(result.size()>10);
        assertNotNull(result.get(0).getCreatedAt());
        assertNotNull(result.get(0).getId());
        assertNotNull(result.get(0).getText());
        assertNotNull(result.get(0).getInReplyToUserId());
    }

    @Test
    public void testFollowAndUnfollow(){
        IUser user = twitterClient.getUserFromUserName("red1");
        twitterClient.follow(user.getId());
        twitterClient.unfollow(user.getId());
    }

    @Test
    public void testLikeTweet(){
        twitterClient.likeTweet("1107533");
    }

    @Test
    public void testSearchTweets7days(){
        Date startDate = DateUtils.truncate(ConverterHelper.dayBeforeNow(5),Calendar.DAY_OF_MONTH);
        Date endDate = DateUtils.truncate(ConverterHelper.dayBeforeNow(1),Calendar.DAY_OF_MONTH);
        List<ITweet> result = twitterClient.searchForTweetsWithin7days("@RedTheOne -RT",startDate, endDate);
        assertTrue(result.size()>10);
        ITweet tweet = result.get(0);
        assertNotNull(tweet.getId());
        assertNotNull(tweet.getText());
        assertNotNull(tweet.getCreatedAt());
        assertNotNull(tweet.getAuthorId());
        assertTrue(tweet.getRetweetCount()>=0);
        assertTrue(tweet.getReplyCount()>=0);
        assertTrue(tweet.getLikeCount()>=0);
        assertNotNull(tweet.getLang());
    }

    /*
    @Test
    public void testSearchTweets30days(){
        Date startDate = DateUtils.truncate(new Date(),Calendar.MONTH);
        Date endDate = DateUtils.addDays(startDate, 1);
        List<ITweet> result = twitterClient.searchForTweetsWithin30days("@RedTheOne -RT",startDate, endDate);
        assertTrue(result.size()>0);
    }

    @Test
    public void testSearchTweetsArchive(){
        Date startDate = DateUtils.truncate(ConverterHelper.dayBeforeNow(60),Calendar.MONTH);
        Date endDate = DateUtils.addDays(startDate, 1);
        List<ITweet> result = twitterClient.searchForTweetsArchive("@RedTheOne -RT",startDate, endDate);
        assertTrue(result.size()>0);
    } */

    @Test
    public void testGetBearerToken(){
        String token = twitterClient.getBearerToken();
        assertNotNull(token);
        assertTrue(token.length()>50);
    }
}