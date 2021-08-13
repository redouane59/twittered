package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetV1;
import io.github.redouane59.twitter.dto.tweet.entities.Entities;
import io.github.redouane59.twitter.dto.tweet.entities.HashtagEntity;
import io.github.redouane59.twitter.dto.tweet.entities.MediaEntity;
import io.github.redouane59.twitter.dto.tweet.entities.SymbolEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UrlEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UserMentionEntity;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TweetDeserializerV1Test {

  private File  tweetFile1 = new File(getClass().getClassLoader().getResource("tests/tweet_example_v1.json").getFile());
  private Tweet tweetV1    = TwitterClient.OBJECT_MAPPER.readValue(tweetFile1, TweetV1.class);

  public TweetDeserializerV1Test() throws IOException {
  }

  @Test
  public void testTweetId() {
    assertEquals("1223664533451026434", tweetV1.getId());
  }

  @Test
  public void testTweetText() {
    assertEquals("@RedTheOne Tu t'es es sorti", tweetV1.getText());
  }

  @Test
  public void testRetweetCount() {
    assertEquals(2, tweetV1.getRetweetCount());
  }

  @Test
  public void testFavoriteCount() {
    assertEquals(1, tweetV1.getLikeCount());
  }

  @Test
  public void testReplyCount() {
    assertEquals(3, tweetV1.getReplyCount());
  }

  @Test
  public void testCreateAt() {
    assertEquals(ConverterHelper.getDateFromTwitterString("Sat Feb 01 17:48:54 +0000 2020"), tweetV1.getCreatedAt());
  }

  @Test
  public void testinReplyToUserId() {
    assertEquals("92073489", tweetV1.getInReplyToUserId());
  }

  @Test
  public void testinReplyToStatusId() {
    assertEquals("1223576831170879489", tweetV1.getInReplyToStatusId());
  }

  @Test
  public void testLang() {
    assertEquals("fr", tweetV1.getLang());
  }

  @Test
  public void testUser() {
    assertNotNull(tweetV1.getUser());
    assertEquals("723996356", tweetV1.getUser().getId());
    assertEquals("naiim75012", tweetV1.getUser().getName());
    assertEquals("France", tweetV1.getUser().getLocation());
    assertEquals(6339, tweetV1.getUser().getFollowersCount());
    assertEquals(392, tweetV1.getUser().getFollowingCount());
    assertEquals(83425, tweetV1.getUser().getTweetCount());
    assertEquals(ConverterHelper.getDateFromTwitterString("Sun Jul 29 13:24:02 +0000 2012"), tweetV1.getUser().getDateOfCreation());
  }

  @Test
  public void testEntities() {
    Entities entities = tweetV1.getEntities();
    assertNotNull(entities);

    List<? extends SymbolEntity> cashtags = entities.getSymbols();
    assertNotNull(cashtags);
    assertEquals(1, cashtags.size());

    SymbolEntity e = cashtags.get(0);
    assertNotNull(e);
    assertEquals(12, e.getStart());
    assertEquals(17, e.getEnd());
    assertEquals("twtr", e.getText());

    List<? extends HashtagEntity> hashtags = entities.getHashtags();
    assertNotNull(hashtags);
    assertEquals(1, hashtags.size());

    HashtagEntity h = hashtags.get(0);
    assertNotNull(h);
    assertEquals(178, h.getStart());
    assertEquals(189, h.getEnd());
    assertEquals("TwitterAPI", h.getText());

    List<? extends UserMentionEntity> mentions = entities.getUserMentions();
    assertNotNull(mentions);
    assertEquals(1, mentions.size());

    TweetV1.UserMentionEntityV1 m = (TweetV1.UserMentionEntityV1) mentions.get(0);
    assertNotNull(m);
    assertEquals(8, m.getStart());
    assertEquals(19, m.getEnd());
    assertEquals("Penn Med CDH", m.getName());
    assertEquals("PennMedCDH", m.getScreenName());
    assertEquals(1615654896, m.getId());

    List<? extends UrlEntity> urls = entities.getUrls();
    UrlEntity                 u    = urls.get(0);
    assertNotNull(u);
    assertEquals(62, u.getStart());
    assertEquals(85, u.getEnd());
    assertEquals("https://t.co/D0n7a53c2l", u.getUrl());
    assertEquals("http://bit.ly/18gECvy", u.getExpandedUrl());
    assertEquals("https://www.youtube.com/watch?v=oHg5SJYRHA0", u.getUnwoundedUrl());
    assertEquals("bit.ly/18gECvy", u.getDisplayUrl());
    assertEquals(200, u.getStatus());
    assertEquals("http://www.facebook.com/rickroll548 As long as trolls are still trolling, the Rick will never stop rolling.", u.getDescription());
    assertEquals("RickRoll'D", u.getTitle());

  }

  @Test
  public void testEntitiesMedia() {
    List<? extends MediaEntity> media = tweetV1.getMedia();
    assertNotNull(media);
    assertEquals(1, media.size());

    MediaEntity e = media.get(0);
    assertNotNull(e);
    assertEquals(219, e.getStart());
    assertEquals(242, e.getEnd());
    assertEquals(1293565706408038401L, e.getId());
    assertEquals("https://pbs.twimg.com/ext_tw_video_thumb/1293565706408038401/pu/img/66P2dvbU4a02jYbV.jpg", e.getMediaUrl());
    assertEquals("https://t.co/KaFSbjWUA8", e.getUrl());
    assertEquals("pic.twitter.com/KaFSbjWUA8", e.getDisplayUrl());
    assertEquals("https://twitter.com/TwitterDev/status/1293593516040269825/video/1", e.getExpandedUrl());
    assertEquals("photo", e.getType());

    TweetV1.MediaEntityV1 ev1 = (TweetV1.MediaEntityV1) e;
    assertNotNull(ev1);
    assertNotNull(ev1.getSizes());

    checkMediaSizeContent(ev1.getSizes(), "thumb", 150, 150, "crop");
    checkMediaSizeContent(ev1.getSizes(), "medium", 1200, 675, "fit");
    checkMediaSizeContent(ev1.getSizes(), "small", 680, 383, "fit");
    checkMediaSizeContent(ev1.getSizes(), "large", 1280, 720, "fit");
  }

  private void checkMediaSizeContent(Map<String, TweetV1.MediaSize> map, String key, int w, int h, String resize) {
    TweetV1.MediaSize ms = map.get(key);
    assertNotNull(ms);
    assertEquals(h, ms.getHeight());
    assertEquals(w, ms.getWidth());
    assertEquals(resize, ms.getResize());
  }
}
