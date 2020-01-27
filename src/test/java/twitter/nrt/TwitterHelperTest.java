package twitter.nrt;

import com.socialmediaraiser.core.RelationType;
import com.socialmediaraiser.core.twitter.*;
import com.socialmediaraiser.core.twitter.helpers.dto.ConverterHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TwitterHelperTest {

    private TwitterHelper twitterHelper = new TwitterHelper();

    @Test
    public void testGetFollowingIdsById() {
        List<String> followings = twitterHelper.getFollowingIds("882266619115864066");
        assertTrue(followings.size() > 200);
    }

    @Test
    public void testGetFollowersIdsById() {
        List<String> followers = twitterHelper.getFollowerIds("882266619115864066");
        assertTrue(followers.size() > 200);
    }

    @Test
    public void testGetFollowersUsersById() {
        List<AbstractUser> followers = twitterHelper.getFollowerUsers("882266619115864066");
        assertTrue(followers.size() > 200);
    }

    @Test
    public void testFriendshipByIdYes() {
        String userId1 = "92073489";
        String userId2 = "723996356";
        RelationType result = twitterHelper.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void getUserByUserName() {
        String userName = "RedTheOne";
        AbstractUser result = twitterHelper.getUserFromUserName(userName);
        assertEquals("92073489", result.getId());
        userName = "RedouaneBali";
        result = twitterHelper.getUserFromUserName(userName);
        assertEquals("RedouaneBali", result.getUsername());
    }

    @Test
    public void testFriendshipByIdNo() {
        String userId1 = "92073489";
        String userId2 = "1976143068";
        RelationType result = twitterHelper.getRelationType(userId1, userId2);
        assertNotEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void testGetUserInfoName() {
        String userId = "92073489";
        AbstractUser user = twitterHelper.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
    }

    @Test
    public void testGetUserInfoId() {
        String userId = "92073489";
        AbstractUser user = twitterHelper.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
    }

    @Test
    public void testGetUserInfoFavouritesDateOfCreation() {
        String userId = "92073489";
        AbstractUser user = twitterHelper.getUserFromUserId(userId);
        assertTrue(user.getDateOfCreation() != null);
    }

    @Test
    public void testGetUserInfoStatusesCount() {
        String userId = "92073489";
        AbstractUser user = twitterHelper.getUserFromUserId(userId);
        assertTrue(user.getTweetCount() > 0);
    }


    @Test
    public void testGetUserInfoLastUpdate() {
        String userId = "92073489";
        AbstractUser user = twitterHelper.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
        assertTrue(user.getLastUpdate() != null);
    }

    @Test
    public void testGetUserInfoFollowingRatio() {
        String userId = "92073489";
        User user = (User) twitterHelper.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
        assertTrue(user.getFollowersRatio() > 1);
    }

    @Test
    public void testGetUserWithCache() {
        String userId = "92073489";
        AbstractUser user = twitterHelper.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
        user = twitterHelper.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
    }

    @Test
    public void testGetUsersFromUserIds() {
        List<String> ids = new ArrayList<>();
        ids.add("92073489"); // RedTheOne
        ids.add("22848599"); // Soltana
        List<AbstractUser> result = twitterHelper.getUsersFromUserIds(ids);
        assertEquals("RedTheOne", result.get(0).getUsername());
        assertEquals("Soltana", result.get(1).getUsername());
    }

    @Test
    public void testGetRateLimitStatus() {
        assertNotEquals(null, twitterHelper.getRateLimitStatus());
    }

    @Test
    public void testRelationBetweenUsersIdFriends() {
        String userId1 = "92073489";
        String userId2 = "723996356";
        RelationType result = twitterHelper.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void testRelationBetweenUsersIdNone() {
        String userId1 = "92073489";
        String userId2 = "1976143068";
        RelationType result = twitterHelper.getRelationType(userId1, userId2);
        assertEquals(RelationType.NONE, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollowing() {
        String userId1 = "92073489";
        String userId2 = "126267113";
        RelationType result = twitterHelper.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWING, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollower() {
        String userId1 = "92073489";
        String userId2 = "1218125226095054848";
        RelationType result = twitterHelper.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWER, result);
    }

    @Test
    public void testGetRetweetersId() {
        String tweetId = "1078358350000205824";
        assertTrue(twitterHelper.getRetweetersId(tweetId).size() > 10);
    }

    @Test
    public void getLastUpdate() {
        String userId = "92073489";
        AbstractUser user = twitterHelper.getUserFromUserId(userId);
        Date now = new Date();
        Date lastUpdate = user.getLastUpdate();
        long diffDays = (now.getTime() - lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
        assertTrue(diffDays < 15);
    }

    @Test
    public void getMostRecentTweets(){
        String userId = "92073489";
        AbstractUser user = twitterHelper.getUserFromUserId(userId);
        assertFalse(user.getMostRecentTweet().isEmpty());
    }


    @Test
    public void testGetLastTweetByUserName() {
        String userName = "RedTheOne";
        List<Tweet> response = twitterHelper.getUserLastTweets(userName, 2);
        assertTrue(response.get(0).getLang().equals("fr")
                || response.get(1).getLang().equals("fr"));
    }

    @Test
    public void testGetLastTweetByUserId() {
        String userId = "92073489";
        List<Tweet> response = twitterHelper.getUserLastTweets(userId, 3);
        assertTrue(response.get(0).getLang().equals("fr")
                || response.get(1).getLang().equals("fr"));
    }

    @Test
    @Disabled
    public void testSearchForTweetsFull() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmm");
        String strdate1 = "201901010000";
        String strdate2 = "202001010000";
        List<Tweet> results = null;
        results = twitterHelper.searchForTweets("@redtheone @demi_sword", 10, strdate1, strdate2,
                this.twitterHelper.getUrlHelper().getSearchTweetsUrlFull());
        assertNotNull(results);
        assertTrue(results.size() > 0);
    }

    @Test
    public void testSearchForTweets() {
        List<Tweet> results = twitterHelper.searchForLast100Tweets30days("@RedTheOne -RT",
                ConverterHelper.getStringFromDate(ConverterHelper.minutesBefore(75)));
        assertNotNull(results);
        assertTrue(results.size() > 0);
    }

}