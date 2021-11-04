package io.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.RelationType;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.list.TwitterList;
import io.github.redouane59.twitter.dto.others.BlockResponse;
import io.github.redouane59.twitter.dto.tweet.LikeResponse;
import io.github.redouane59.twitter.dto.tweet.RetweetResponse;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetParameters;
import io.github.redouane59.twitter.dto.tweet.TweetParameters.Reply;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserActionResponse;
import io.github.redouane59.twitter.dto.user.UserList;
import java.time.LocalDateTime;
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
  public void testMuteAndUnmuteUser() {
    String             userId = "1324848235714736129";
    UserActionResponse result = twitterClient.muteUser(userId);
    assertTrue(result.getData().isMuting());
    result = twitterClient.unmuteUser(userId);
    assertFalse(result.getData().isMuting());
  }

  @Test
  public void testMuteLookup() {
    UserList result = twitterClient.getMutedUsers();
    assertNotNull(result.getData());
    assertNotNull(result.getMeta());
    assertNotNull(result.getData().get(0));
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getName());
    assertNotNull(result.getData().get(0).getCreatedAt());
  }

  @Test
  public void testGetUserIdFromAccessToken() {
    assertEquals(userId, twitterClient.getUserIdFromAccessToken());
  }

  @Test
  public void testRetweetAndUnretweetTweet() {
    RetweetResponse resultRT = twitterClient.retweetTweet("1413515358766452738");
    assertNotNull(resultRT);
    assertTrue(resultRT.getData().isRetweeted());
    resultRT = twitterClient.unretweetTweet("1413515358766452738");
    assertNotNull(resultRT);
    assertFalse(resultRT.getData().isRetweeted());
  }

  @Test
  public void testCreateListAndAddRemoveMemberAndPinUnpinListAndDeleteList() {
    String listName = "Test";
    // create list
    TwitterList result = twitterClient.createList(listName, "desc", true);
    assertEquals(listName, result.getData().getName());
    String listId = result.getData().getId();
    assertNotNull(listId);
    // edit list
    boolean updateResult = twitterClient.updateList(listId, listName + "X", "desc2", false);
    assertTrue(updateResult);
    // add member
    boolean result2 = twitterClient.addListMember(listId, userId);
    assertTrue(result2);
    // remove member
    result2 = twitterClient.removeListMember(listId, userId);
    assertFalse(result2);
    // pin list
    result2 = twitterClient.pinList(listId);
    assertTrue(result2);
    // unpin list
    result2 = twitterClient.unpinList(listId);
    assertFalse(result2);
    // delete list
    result2 = twitterClient.deleteList(listId);
    assertTrue(result2);
  }

  @Test
  public void testFollowAndUnfollowList() {
    String  listId = "1449313282892910592";
    boolean result = twitterClient.followList(listId);
    assertTrue(result);
    result = twitterClient.unfollowList(listId);
    assertFalse(result);
  }

  @Test
  public void testPostAndDeleteTweet() {
    String text       = "Test post Tweet V2 at " + LocalDateTime.now() + " #TwitterAPI";
    Tweet  resultPost = twitterClient.postTweet(text);
    assertNotNull(resultPost);
    assertNotNull(resultPost.getId());
    assertEquals(text, resultPost.getText());
    boolean result = twitterClient.deleteTweet(resultPost.getId());
    assertTrue(result);
  }

  @Test
  public void testPostQuoteTweetAndDeleteTweet() {
    String text = "Test postTweet v2 with parameters";
    TweetParameters parameters = TweetParameters.builder()
                                                .text(text)
                                                .quoteTweetId("1456359240768045059")
                                                .build();
    Tweet resultPost = twitterClient.postTweet(parameters);
    assertNotNull(resultPost);
    assertNotNull(resultPost.getId());
    assertEquals(text, resultPost.getText());
    boolean result = twitterClient.deleteTweet(resultPost.getId());
    assertTrue(result);
  }

  @Test
  public void testPostReplyTweetAndDeleteTweet() {
    String text = "Test postTweet v2 with parameters";
    TweetParameters parameters = TweetParameters.builder()
                                                .text(text)
                                                .reply(
                                                    Reply.builder()
                                                         .inReplyToTweetId("1456359240768045059")
                                                         .build()
                                                )
                                                .build();
    Tweet resultPost = twitterClient.postTweet(parameters);
    assertNotNull(resultPost);
    assertNotNull(resultPost.getId());
    assertEquals(text, resultPost.getText());
    boolean result = twitterClient.deleteTweet(resultPost.getId());
    assertTrue(result);
  }

}