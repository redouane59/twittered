package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.ContextAnnotation;
import io.github.redouane59.twitter.dto.tweet.ReplySettings;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.dto.tweet.TweetV2.Place;
import io.github.redouane59.twitter.dto.tweet.entities.HashtagEntity;
import io.github.redouane59.twitter.dto.tweet.entities.MediaEntity;
import io.github.redouane59.twitter.dto.tweet.entities.SymbolEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UrlEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UserMentionEntity;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TweetDeserializerV2Test {

  private File  tweetFile1 = new File(getClass().getClassLoader().getResource("tests/tweet_example_v2.json").getFile());
  private Tweet tweetv2    = TwitterClient.OBJECT_MAPPER.readValue(tweetFile1, TweetV2.class);

  public TweetDeserializerV2Test() throws IOException {
  }

  @Test
  public void testTweetId() {
    assertEquals("1224044675406925824", tweetv2.getId());
  }

  @Test
  public void testTweetText() {
    assertEquals(
        "@RedouaneBali @TwitterAPI Try to use some function construct of the recebt Java version. It is a good train to improve your procedural code :)",
        tweetv2.getText());
  }

  @Test
  public void testRetweetCount() {
    assertEquals(0, tweetv2.getRetweetCount());
  }

  @Test
  public void testLikeCount() {
    assertEquals(2, tweetv2.getLikeCount());
  }

  @Test
  public void testReplyCount() {
    assertEquals(2, tweetv2.getReplyCount());
  }

  @Test
  public void testQuoteCount() {
    assertEquals(0, tweetv2.getQuoteCount());
  }

  @Test
  public void testCreateAt() {
    assertEquals(ConverterHelper.getDateFromTwitterStringV2("2020-02-02T18:59:26.000Z"), tweetv2.getCreatedAt());
  }

  @Test
  public void testinReplyToUserId() {
    assertEquals("1120050519182016513", tweetv2.getInReplyToUserId());
  }

  @Test
  public void testinReplyToStatusId() {
    assertEquals("1224041905333379073", tweetv2.getInReplyToStatusId());
  }

  @Test
  public void testLang() {
    assertEquals("en", tweetv2.getLang());
  }

  @Test
  public void testUser() {
    User user = tweetv2.getUser();
    assertNotNull(user);
    assertEquals("marcomornati", user.getName());
    assertEquals("9920272", user.getId());
    assertEquals(433, user.getFollowersCount());
    assertEquals(834, user.getFollowingCount());
  }

  @Test
  public void testConversationId() {
    assertEquals("1224041905333379073", tweetv2.getConversationId());
  }

  @Test
  public void testContextAnnotations() {
    List<ContextAnnotation> contextAnnotationList = tweetv2.getContextAnnotations();
    assertNotNull(contextAnnotationList);
    assertEquals(2, contextAnnotationList.size());
    ContextAnnotation contextAnnotation1 = contextAnnotationList.get(0);
    ContextAnnotation contextAnnotation2 = contextAnnotationList.get(1);
    assertEquals("65", contextAnnotation1.getDomain().getId());
    assertEquals("Interests and Hobbies Vertical", contextAnnotation1.getDomain().getName());
    assertEquals("Top level interests and hobbies groupings, like Food or Travel", contextAnnotation1.getDomain().getDescription());
    assertEquals("848921413196984320", contextAnnotation2.getEntity().getId());
    assertEquals("Computer programming", contextAnnotation2.getEntity().getName());
    assertEquals("Computer programming", contextAnnotation2.getEntity().getDescription());
  }

  @Test
  public void testReplySettings() {
    Assertions.assertEquals(ReplySettings.EVERYONE, tweetv2.getReplySettings());
  }

  @Test
  public void testGeo() {
    assertNotNull(tweetv2.getGeo());
    assertEquals("01a9a39529b27f36", tweetv2.getGeo().getPlaceId());
    assertEquals("Point", tweetv2.getGeo().getCoordinates().getType());
    assertEquals(-73.99960455, tweetv2.getGeo().getCoordinates().getCoordinates()[0]);
    assertEquals(40.74168819, tweetv2.getGeo().getCoordinates().getCoordinates()[1]);
  }

  @Test
  public void testMedia() {
    assertNotNull(tweetv2.getAttachments());
    assertNotNull(tweetv2.getAttachments().getMediaKeys());
    assertEquals("3_1365362339449561088", tweetv2.getAttachments().getMediaKeys()[0]);
  }

  @Test
  public void testSource() {
    assertNotNull(tweetv2.getSource());
    assertEquals("Twitter for Android", tweetv2.getSource());
  }

  @Test
  public void testSerialization() throws JsonProcessingException {
    String tweetAsString = TwitterClient.OBJECT_MAPPER.writeValueAsString(tweetv2);
    assertNotNull(tweetAsString);
    assertTrue(tweetAsString.contains("Try to use some function"));
  }

  @Test
  public void testEntities() {
    TweetV2            tweet    = (TweetV2) tweetv2;
    TweetV2.EntitiesV2 entities = tweet.getData().getEntities();
    assertNotNull(entities);

    List<? extends SymbolEntity> cashtags = entities.getSymbols();
    assertNotNull(cashtags);
    assertEquals(1, cashtags.size());

    SymbolEntity e = cashtags.get(0);
    assertNotNull(e);
    assertEquals(18, e.getStart());
    assertEquals(23, e.getEnd());
    assertEquals("twtr", e.getText());

    List<? extends HashtagEntity> hashtags = entities.getHashtags();
    assertNotNull(hashtags);
    assertEquals(1, hashtags.size());

    HashtagEntity h = hashtags.get(0);
    assertNotNull(h);
    assertEquals(0, h.getStart());
    assertEquals(17, h.getEnd());
    assertEquals("blacklivesmatter", h.getText());

    List<? extends UserMentionEntity> mentions = entities.getUserMentions();
    assertNotNull(mentions);
    assertEquals(2, mentions.size());

    UserMentionEntity m = mentions.get(0);
    assertNotNull(m);
    assertEquals(0, m.getStart());
    assertEquals(13, m.getEnd());
    assertEquals("RedouaneBali", m.getText());

    m = mentions.get(1);
    assertNotNull(m);
    assertEquals(14, m.getStart());
    assertEquals(25, m.getEnd());
    assertEquals("TwitterAPI", m.getText());

    List<? extends UrlEntity> urls = entities.getUrls();
    UrlEntity                 u    = urls.get(0);
    assertNotNull(u);
    assertEquals(44, u.getStart());
    assertEquals(67, u.getEnd());
    assertEquals("https://t.co/crkYRdjUB0", u.getUrl());
    assertEquals("https://twitter.com", u.getExpandedUrl());
    assertEquals("https://twitter.com", u.getUnwoundedUrl());
    assertEquals("twitter.com", u.getDisplayUrl());
    assertEquals(200, u.getStatus());
    assertEquals("From breaking news and entertainment to sports and politics, get the full story with all the live commentary.", u.getDescription());
    assertEquals("bird", u.getTitle());
  }

  @Test
  public void testEntitiesMedia() {
    List<? extends MediaEntity> media = tweetv2.getMedia();
    assertNotNull(media);
    assertEquals(1, media.size());

    MediaEntity e = media.get(0);
    assertNotNull(e);
    assertEquals(-1, e.getStart());
    assertEquals(-1, e.getEnd());
    assertEquals(-1, e.getId());
    assertEquals("https://pbs.twimg.com/ext_tw_video_thumb/1293565706408038401/pu/img/66P2dvbU4a02jYbV.jpg", e.getMediaUrl());
    assertEquals("https://pbs.twimg.com/ext_tw_video_thumb/1293565706408038401/pu/img/66P2dvbU4a02jYbV.jpg", e.getUrl());
    assertEquals("https://pbs.twimg.com/ext_tw_video_thumb/1293565706408038401/pu/img/66P2dvbU4a02jYbV.jpg", e.getDisplayUrl());
    assertEquals("https://pbs.twimg.com/ext_tw_video_thumb/1293565706408038401/pu/img/66P2dvbU4a02jYbV.jpg", e.getExpandedUrl());
    assertEquals("video", e.getType());

    TweetV2.MediaEntityV2 ev2 = (TweetV2.MediaEntityV2) e;
    assertNotNull(ev2);
    assertEquals("3_1365362339449561088", ev2.getKey());
    assertTrue(Arrays.asList(tweetv2.getAttachments().getMediaKeys()).contains(ev2.getKey()));
    assertEquals(34875, ev2.getDuration());
    assertEquals(720, ev2.getHeight());
    assertEquals(1280, ev2.getWidth());
    assertEquals("test", ev2.getAltText());
  }

  @Test
  public void testIncludesPlace() {
    List<Place> places = tweetv2.getPlaces();
    Place       place  = places.get(0);
    assertEquals("US", place.getCountryCode());
    assertEquals("Manhattan", place.getName());
    assertEquals("01a9a39529b27f36", place.getId());
    assertEquals("city", place.getPlaceType());
    assertEquals("United States", place.getCountry());
    assertEquals("Manhattan, NY", place.getFullName());
    assertEquals("Feature", place.getGeo().getType());
    assertTrue(place.getGeo().getBbox().size() > 0);
  }
}
