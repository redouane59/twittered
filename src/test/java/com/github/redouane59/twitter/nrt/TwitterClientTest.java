package com.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.github.redouane59.RelationType;
import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.others.RequestTokenDTO;
import com.github.redouane59.twitter.dto.tweet.ITweet;
import com.github.redouane59.twitter.dto.tweet.TweetDTOv1;
import com.github.redouane59.twitter.dto.tweet.TweetType;
import com.github.redouane59.twitter.dto.user.IUser;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class TwitterClientTest {

    private static TwitterClient twitterClient;

    @BeforeAll
    public static void init() throws IOException {
        String credentialPath = "C:/Users/Perso/Documents/GitHub/twitter-credentials.json";
        twitterClient             = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                                          .readValue(new File(credentialPath), TwitterCredentials.class));
    }

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
    public void getUsersByUserNames(){
        List<IUser> result = twitterClient.getUsersFromUserNames(List.of("Zidane", "Ronaldo", "RedTheOne"));
        assertEquals(3, result.size());
        assertEquals("92073489", result.get(2).getId());
    }

    @Test
    public void getUsersByUserIds(){
        List<IUser> result = twitterClient.getUsersFromUserIds(List.of("22848599","1976143068","92073489"));
        assertEquals(3, result.size());
        assertEquals("RedTheOne", result.get(2).getName());
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
        assertNotNull(user.getDateOfCreation());
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
    public void testGetTweetsByIds(){
        List<String> tweetIds = List.of("1294174710624849921,1294380029430960128,1294375095746666496");
        List<ITweet> tweets = twitterClient.getTweets(tweetIds);
        assertTrue(tweets.size()>0);
        assertTrue(tweets.get(0).getText().length()>0);
        assertTrue(tweets.get(1).getText().length()>0);
        assertTrue(tweets.get(2).getText().length()>0);
    }

    @Test
    public void testGetOauth1Token(){
        TwitterClient.TWITTER_CREDENTIALS.setAccessToken("");
        TwitterClient.TWITTER_CREDENTIALS.setAccessTokenSecret("");
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
        assertEquals(RelationType.NONE, twitterClient.getRelationType("RedThOne","red1"));
    }

    @Test
    public void testLikeTweet(){
        Exception ex = null;
        try {
            twitterClient.likeTweet("1107533");
        } catch (Exception e) {
            ex = e;
        }
        assertNull(ex);
    }

    @Test
    public void testSearchTweets7days(){
        List<ITweet> result = twitterClient.searchForTweetsWithin7days("@RedTheOne -RT");
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

    @Test
    public void testGetFavorites(){
        int count = 1500;
        List<ITweet> favorites = twitterClient.getFavorites("92073489", count);
        assertNotNull(favorites);
        assertTrue(favorites.size()>count);
    }

    @Test
    public void testGetTweetType(){
        assertEquals(TweetType.QUOTED, this.twitterClient.getTweet("1267115291991068673").getTweetType());
        assertEquals(TweetType.REPLIED_TO, this.twitterClient.getTweet("1267132388632604673").getTweetType());
        assertEquals(null, this.twitterClient.getTweet("1267010053040672768").getTweetType());
    }

    @Test
    public void testGetTweetIdWithTwoTypes(){
        assertEquals("1264255917043920904", this.twitterClient.getTweet("1264256827690270722").getInReplyToStatusId(TweetType.RETWEETED));
        assertEquals("1263783602485157889", this.twitterClient.getTweet("1264256827690270722").getInReplyToStatusId(TweetType.QUOTED));
    }

    /*
    @Test
    public void testSearchTweets30days(){
        LocalDateTime startDate = DateUtils.truncate(new Date(),Calendar.MONTH);
        LocalDateTime endDate = DateUtils.addDays(startDate, 1);
        List<ITweet> result = twitterClient.searchForTweetsWithin30days("@RedTheOne -RT",startDate, endDate);
        assertTrue(result.size()>0);
    }

    @Test
    public void testSearchTweetsArchive(){
        LocalDateTime startDate = DateUtils.truncate(ConverterHelper.dayBeforeNow(60),Calendar.MONTH);
        LocalDateTime endDate = DateUtils.addDays(startDate, 1);
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