package io.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.RelationType;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.others.BlockResponse;
import io.github.redouane59.twitter.dto.tweet.LikeResponse;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserActionResponse;
import io.github.redouane59.twitter.dto.user.UserList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ITwitterClientV2AuthenticatedTest {

  private static TwitterClient twitterClient;
  private static String        userId;


  @BeforeAll
  public static void init() {
    twitterClient = new TwitterClient();
    userId        = twitterClient.getUserIdFromAccessToken();
  }

  @Test
  public void testHideAndUnideReply() {
    String  tweetId = "1381279105014960130"; // find a tweet where someone has replied to your tweet
    boolean reply   = twitterClient.hideReply(tweetId, true);
    assertTrue(reply);
    reply = twitterClient.hideReply(tweetId, false);
    assertFalse(reply);
  }

  @Test
  public void testFollowAndUnfollow() {
    User               user               = twitterClient.getUserFromUserName("red1");
    UserActionResponse userActionResponse = twitterClient.follow(user.getId());
    assertTrue(userActionResponse.getData().isFollowing());
    assertFalse(userActionResponse.getData().isPendingFollow());
    UserActionResponse unfollowResponse = twitterClient.unfollow(user.getId());
    assertFalse(unfollowResponse.getData().isFollowing());
    Assertions.assertEquals(RelationType.NONE, twitterClient.getRelationType("92073489", "66533"));
  }

  @Test
  public void testBlockAndUnblockUser() {
    String        targetId = "456777022";
    BlockResponse response = twitterClient.blockUser(targetId);
    assertTrue(response.getData().isBlocking());
    response = twitterClient.unblockUser(targetId);
    assertFalse(response.getData().isBlocking());
  }

  @Test
  public void testGetBlockingUsers() {
    UserList result = twitterClient.getBlockedUsers();
    assertNotNull(result);
    assertTrue(result.getData().size() > 0);
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getName());
  }

  @Test
  public void testLikeTweet() {
    LikeResponse likedTweet = twitterClient.likeTweet("1107533");
    assertTrue(likedTweet.getData().isLiked());
    LikeResponse unlikedTweet = twitterClient.unlikeTweet("1107533");
    assertFalse(unlikedTweet.getData().isLiked());
  }

  @Test
  public void testGetUserIdFromAccessToken() {
    assertEquals(this.userId, twitterClient.getUserIdFromAccessToken());
  }

}