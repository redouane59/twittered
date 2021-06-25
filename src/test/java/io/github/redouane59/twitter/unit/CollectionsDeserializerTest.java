package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.collections.CollectionsResponse;
import io.github.redouane59.twitter.dto.collections.CollectionsResponse.Objects.Timelines.Timeline;
import io.github.redouane59.twitter.dto.collections.CollectionsResponse.Response.Error;
import io.github.redouane59.twitter.dto.collections.CollectionsResponse.Response.TimelineTweet;
import io.github.redouane59.twitter.dto.tweet.TweetV1;
import io.github.redouane59.twitter.dto.user.UserV1;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for the various collections endpoints:
 * <ul>
 *   <li><a href="https://developer.twitter.com/en/docs/twitter-api/v1/tweets/curate-a-collection/api-reference/post-collections-entries-curate">Collections Curate</a>  tests/collections_curate_example.json</li>
 *   <li><a href="https://developer.twitter.com/en/docs/twitter-api/v1/tweets/curate-a-collection/api-reference/get-collections-entries">Collections Entries</a>  tests/collections_entries_example.json</a></li>
 *   <li><a https://developer.twitter.com/en/docs/twitter-api/v1/tweets/curate-a-collection/api-reference/post-collections-create">Collections Create</a>  tests/collections_create_example.json</a></li>
 *   <li><a href="https://developer.twitter.com/en/docs/twitter-api/v1/tweets/curate-a-collection/api-reference/post-collections-destroy">Collections Destroy</a>  tests/collections_destroy_example.json</a></li>
 * </ul>
 *
 * <br>
 * The <a href="https://developer.twitter.com/en/docs/twitter-api/v1/tweets/curate-a-collection/overview/response_structures">Response Structures</a>are a bit different to traditional trwitter api
 */
public class CollectionsDeserializerTest {

  // Example response from a call to curate a collection
  private final File
      collectionsCurateFile     =
      new File(Objects.requireNonNull(getClass().getClassLoader().getResource("tests/collections_curate_example.json")).getFile());
  private final CollectionsResponse
      collectionsCurateResponse =
      TwitterClient.OBJECT_MAPPER.readValue(collectionsCurateFile, CollectionsResponse.class);

  // Example response from a call to get entries in a collection
  private final File
      collectionsEntriesFile     =
      new File(Objects.requireNonNull(getClass().getClassLoader().getResource("tests/collections_entries_example.json")).getFile());
  private final CollectionsResponse
      collectionsEntriesResponse =
      TwitterClient.OBJECT_MAPPER.readValue(collectionsEntriesFile, CollectionsResponse.class);

  // Example response from a call to create a collection
  private final File
      collectionsCreateFile     =
      new File(Objects.requireNonNull(getClass().getClassLoader().getResource("tests/collections_create_example.json")).getFile());
  private final CollectionsResponse
      collectionsCreateResponse =
      TwitterClient.OBJECT_MAPPER.readValue(collectionsCreateFile, CollectionsResponse.class);

  // Example response from a call to destroy a collection
  private final File
      collectionsDestroyFile     =
      new File(Objects.requireNonNull(getClass().getClassLoader().getResource("tests/collections_destroy_example.json")).getFile());
  private final CollectionsResponse
      collectionsDestroyResponse =
      TwitterClient.OBJECT_MAPPER.readValue(collectionsDestroyFile, CollectionsResponse.class);


  public CollectionsDeserializerTest() throws IOException {
  }

  @Test
  public void testCollectionsCurateHasErrors() {
    assertTrue(collectionsCurateResponse.hasErrors());
  }

  @Test
  public void testCollectionsDestroy() {
    assertTrue(collectionsDestroyResponse.isDestroyed());
  }

  @Test
  public void testCollectionsCurateErrors() {
    assertEquals(4, collectionsCurateResponse.getResponse().getErrors().size());
    Error error = collectionsCurateResponse.getResponse().getErrors().get(0);
    assertEquals("duplicate", error.getReason());
    assertEquals("add", error.getChange().getOperation());
    assertEquals("390897780949925889", error.getChange().getTweetId());

  }

  @Test
  public void testCollectionsCreateTimelineId() {
    assertEquals("custom-1349447047020212224", collectionsCreateResponse.getResponse().getTimeLineId());
  }

  @Test
  public void testCollectionsCreateTimeline() {
    assertEquals("custom-1349447047020212224", collectionsCreateResponse.getResponse().getTimeLineId());
    assertNotNull(collectionsCreateResponse.getObjects().getTimelines().getTimelineDetailsDetails().get("custom-1349447047020212224"));
    assertEquals(1, collectionsCreateResponse.getObjects().getTimelines().get().size());

    Timeline timeline = collectionsCreateResponse.getObjects().getTimelines().get().get(0);

    assertEquals("https://twitter.com/LlSTENlNG_PARTY/timelines/1349447047020212224", timeline.getCollectionUrl());
    assertEquals("1255057822959747073", timeline.getUserId());
  }

  @Test
  public void testCollectionsCreateUsers() {
    assertEquals(1, collectionsCreateResponse.getObjects().getUsers().get().size());
    UserV1 userV1 = collectionsCreateResponse.getObjects().getUsers().get().get(0);
    Assertions.assertEquals(userV1, collectionsCreateResponse.getObjects().getUsers().getUserDetails().get(userV1.getId()));
    assertEquals("Tim's Listening Party", userV1.getDisplayedName());
    assertEquals("Updates and info about #TimsTwitterListeningParty", userV1.getDescription());
    assertEquals(47244, userV1.getFollowersCount());
    assertEquals(16248, userV1.getTweetCount());

  }

  @Test
  public void testCollectionsEntriesTimeline() {
    assertEquals("custom-1348368035900493824", collectionsEntriesResponse.getResponse().getTimeLineId());
    assertEquals(3, collectionsEntriesResponse.getResponse().getTimeline().size());
    TimelineTweet timelineTweet = collectionsEntriesResponse.getResponse().getTimeline().get(0);
    assertEquals("HBgGY3VzdG9tFoDAuOW15a62JQAA", timelineTweet.getFeatureContext());
    assertEquals("1348264201937149953", timelineTweet.getTweet().getId());
    assertEquals("7875107834917625854", timelineTweet.getTweet().getSortIndex());
  }


  @Test
  public void testCollectionsEntriesObjectTweets() {
    assertNotNull(collectionsEntriesResponse.getObjects());
    assertEquals(3, collectionsEntriesResponse.getObjects().getTweets().getTweetDetails().size());
    assertEquals(3, collectionsEntriesResponse.getObjects().getTweets().get().size());
    TweetV1 firstTweet = collectionsEntriesResponse.getObjects().getTweets().get().get(0);
    // mapped by tweet id
    Assertions.assertEquals(collectionsEntriesResponse.getObjects().getTweets().getTweetDetails().get(firstTweet.getId()), firstTweet);

    assertEquals("Kung fu city", firstTweet.getText());
  }

  @Test
  public void testCollectionsEntriesPosition() {
    assertNotNull(collectionsEntriesResponse.getResponse());
    assertFalse(collectionsEntriesResponse.getResponse().getPosition().getWasTruncated());
    assertEquals("7875026924025212925", collectionsEntriesResponse.getResponse().getPosition().getMinPosition());
    assertEquals("7875107834917625854", collectionsEntriesResponse.getResponse().getPosition().getMaxPosition());
  }

  @Test
  public void testCollectionsEntriesObjectUsers() {
    assertNotNull(collectionsEntriesResponse.getObjects());
    assertEquals(3, collectionsEntriesResponse.getObjects().getUsers().getUserDetails().size());
    assertEquals(3, collectionsEntriesResponse.getObjects().getUsers().get().size());

    UserV1 firstUser = collectionsEntriesResponse.getObjects().getUsers().get().get(0);
    // mapped by tweet id
    Assertions.assertEquals(collectionsEntriesResponse.getObjects().getUsers().getUserDetails().get(firstUser.getId()), firstUser);

    assertEquals("LlSTENlNG_PARTY", firstUser.getName());
    assertEquals("Tim's Listening Party", firstUser.getDisplayedName());

  }

}
