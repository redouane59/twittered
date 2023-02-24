package io.github.redouane59.twitter.dto.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.twitter.dto.rules.FilteredStreamRulePredicate.RuleBuilderException;
import org.junit.jupiter.api.Test;

class FilteredStreamRulePredicateTest {

  @Test
  void testEmpty() {
    final FilteredStreamRulePredicate p = FilteredStreamRulePredicate.empty();
    assertEquals("", p.toString());
    assertTrue(p.isEmpty());
  }

  @Test
  void testExactPhrase() {
    assertEquals("\"test\"", FilteredStreamRulePredicate.withExactPhrase("test").toString());
  }

  @Test
  void testKeyword() {
    assertEquals("test", FilteredStreamRulePredicate.withKeyword("test").toString());
  }

  @Test
  void testEmoji() {
    assertEquals("\uD83D\uDE1C", FilteredStreamRulePredicate.withEmoji("\uD83D\uDE1C").toString());
  }

  @Test
  void testWithHashtag1() {
    assertEquals("#test", FilteredStreamRulePredicate.withHashtag("test").toString());
  }

  @Test
  void testWithHashtag2() {
    assertEquals("#test", FilteredStreamRulePredicate.withHashtag("#test").toString());
  }

  @Test
  void testWithMention1() {
    assertEquals("@test", FilteredStreamRulePredicate.withMention("test").toString());
  }

  @Test
  void testWithMention2() {
    assertEquals("@test", FilteredStreamRulePredicate.withMention("@test").toString());
  }

  @Test
  void testWithCashtag1() {
    assertEquals("$test", FilteredStreamRulePredicate.withCashtag("test").toString());
  }

  @Test
  void testWithCashtag2() {
    assertEquals("$test", FilteredStreamRulePredicate.withCashtag("$test").toString());
  }

  @Test
  void testWithUser1() {
    assertEquals("from:test", FilteredStreamRulePredicate.withUser("test").toString());
  }

  @Test
  void testWithUser2() {
    assertEquals("from:1541513", FilteredStreamRulePredicate.withUser("1541513").toString());
  }

  @Test
  void testWithReplyTo1() {
    assertEquals("to:test", FilteredStreamRulePredicate.withReplyTo("test").toString());
  }

  @Test
  void testWithReplyTo2() {
    assertEquals("to:1541513", FilteredStreamRulePredicate.withReplyTo("1541513").toString());
  }

  @Test
  void testWithUrl() {
    assertEquals("url:\"https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule\"",
                 FilteredStreamRulePredicate.withUrl("https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule")
                                            .toString());
  }

  @Test
  void testWithRetweetsOf1() {
    assertEquals("retweets_of:test", FilteredStreamRulePredicate.withRetweetsOf("test").toString());
  }

  @Test
  void testWithRetweetsOf2() {
    assertEquals("retweets_of:1541513", FilteredStreamRulePredicate.withRetweetsOf("1541513").toString());
  }

  @Test
  void testWithContext() {
    assertEquals("context:*.799022225751871488", FilteredStreamRulePredicate.withContext("*.799022225751871488").toString());
  }

  @Test
  void testWithEntity() {
    assertEquals("entity:\"Michael Jordan\"", FilteredStreamRulePredicate.withEntity("Michael Jordan").toString());
  }

  @Test
  void testWithConversationId() {
    assertEquals("conversation_id:1334987486343299072", FilteredStreamRulePredicate.withConversationId("1334987486343299072").toString());
  }

  @Test
  void testWithBio() {
    assertEquals("bio:test", FilteredStreamRulePredicate.withBio("test").toString());
  }

  @Test
  void testWithBioName() {
    assertEquals("bio_name:test", FilteredStreamRulePredicate.withBioName("test").toString());
  }

  @Test
  void testWithBioLocation() {
    assertEquals("bio_location:nyc", FilteredStreamRulePredicate.withBioLocation("nyc").toString());
  }

  @Test
  void testWithPlace() {
    assertEquals("place:seattle", FilteredStreamRulePredicate.withPlace("seattle").toString());
  }

  @Test
  void testWithPlaceCountry() {
    assertEquals("place_country:DE", FilteredStreamRulePredicate.withPlaceCountry("DE").toString());
  }

  @Test
  void testWithPointRadius() {
    assertEquals("point_radius:[2.355128 48.861118 16km]", FilteredStreamRulePredicate.withPointRadius(2.355128, 48.861118, "16km").toString());
  }

  @Test
  void testWithBoundingBox() {
    assertEquals("bounding_box:[-105.301758 39.964069 -105.178505 40.09455]",
                 FilteredStreamRulePredicate.withBoundingBox(-105.301758, 39.964069, -105.178505, 40.09455).toString());
  }

  @Test
  void testWithLanguage() {
    assertEquals("lang:de", FilteredStreamRulePredicate.withLanguage("de").toString());
  }

  @Test
  void testNegateRule() {
    assertEquals("-(point_radius:[2.355128 48.861118 16km])",
                 FilteredStreamRulePredicate.withPointRadius(2.355128, 48.861118, "16km").negate().toString());
  }

  @Test
  void testNegateOnEmptyPredicate() {
    final FilteredStreamRulePredicate p = FilteredStreamRulePredicate.empty();
    assertThrows(RuleBuilderException.class, p::negate);
  }

  @Test
  void testCapsule() {
    assertEquals("(point_radius:[2.355128 48.861118 16km])",
                 FilteredStreamRulePredicate.withPointRadius(2.355128, 48.861118, "16km").capsule().toString());
  }

  @Test
  void testCapsuleOnEmptyPredicate() {
    final FilteredStreamRulePredicate p = FilteredStreamRulePredicate.empty().capsule();
    assertTrue(p.isEmpty());
  }

  @Test
  void testIsRetweet() {
    assertEquals("test is:retweet", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.isRetweet()).toString());
  }

  @Test
  void testIsReply() {
    assertEquals("test is:reply", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.isReply()).toString());
  }

  @Test
  void testIsQuote() {
    assertEquals("test is:quote", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.isQuote()).toString());
  }

  @Test
  void testIsVerified() {
    assertEquals("test is:verified", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.isVerified()).toString());
  }

  @Test
  void testIsNullcast() {
    assertEquals("test -is:nullcast", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.isNullcast()).toString());
  }

  @Test
  void testHasHashtags() {
    assertEquals("test has:hashtags", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.hasHashtags()).toString());
  }

  @Test
  void testHasCashtags() {
    assertEquals("test has:cashtags", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.hasCashtags()).toString());
  }

  @Test
  void testHasLinks() {
    assertEquals("test has:links", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.hasLinks()).toString());
  }

  @Test
  void testHasMentions() {
    assertEquals("test has:mentions", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.hasMentions()).toString());
  }

  @Test
  void testHasMedia() {
    assertEquals("test has:media", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.hasMedia()).toString());
  }

  @Test
  void testHasImages() {
    assertEquals("test has:images", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.hasImages()).toString());
  }

  @Test
  void testHasVideos() {
    assertEquals("test has:videos", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.hasVideos()).toString());
  }

  @Test
  void testHasGeo() {
    assertEquals("test has:geo bakery", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.hasGeo("bakery")).toString());
  }

  @Test
  void testSample() {
    assertEquals("test sample:15", FilteredStreamRulePredicate.withKeyword("test").and(FilteredStreamRulePredicate.doSampling(15)).toString());
  }

  @Test
  void testOrWithLeftEmptyPredicate() {
    final FilteredStreamRulePredicate p = FilteredStreamRulePredicate.empty();
    assertEquals("lang:de", p.or(FilteredStreamRulePredicate.withLanguage("de")).toString());
  }

  @Test
  void testOrWithRightEmptyPredicate() {
    final FilteredStreamRulePredicate p = FilteredStreamRulePredicate.withLanguage("de");
    assertEquals("lang:de", p.or(FilteredStreamRulePredicate.empty()).toString());
  }

  @Test
  void testAndWithLeftEmptyPredicate() {
    final FilteredStreamRulePredicate p = FilteredStreamRulePredicate.withLanguage("de");
    assertEquals("lang:de", p.and(FilteredStreamRulePredicate.empty()).toString());
  }

  @Test
  void testComplexQueries1() {
    FilteredStreamRulePredicate p  = FilteredStreamRulePredicate.withExactPhrase("test");
    FilteredStreamRulePredicate p2 = FilteredStreamRulePredicate.withBioName("test");
    FilteredStreamRulePredicate p3 = FilteredStreamRulePredicate.withLanguage("de");
    FilteredStreamRulePredicate p4 = FilteredStreamRulePredicate.withLanguage("en").negate();
    assertEquals("((\"test\" bio_name:test) OR lang:de) OR -(lang:en)", p.and(p2).capsule().or(p3).capsule().or(p4).toString());
  }

  @Test
  void testComplexQueries2() {
    FilteredStreamRulePredicate p  = FilteredStreamRulePredicate.withExactPhrase("test");
    FilteredStreamRulePredicate p2 = FilteredStreamRulePredicate.withBioName("test");
    FilteredStreamRulePredicate p3 = FilteredStreamRulePredicate.withLanguage("de");
    FilteredStreamRulePredicate p4 = FilteredStreamRulePredicate.withLanguage("en").negate();
    assertEquals("((\"test\" bio_name:test) OR lang:de) OR -(lang:en) sample:15",
                 p.and(p2).capsule().or(p3).capsule().or(p4).and(FilteredStreamRulePredicate.doSampling(15)).toString());
  }

  @Test
  void testComplexQueries3() {
    FilteredStreamRulePredicate p  = FilteredStreamRulePredicate.withExactPhrase("test");
    FilteredStreamRulePredicate p2 = FilteredStreamRulePredicate.withBioName("test");
    FilteredStreamRulePredicate p3 = FilteredStreamRulePredicate.withLanguage("de");
    FilteredStreamRulePredicate p4 = FilteredStreamRulePredicate.withLanguage("en").negate();
    assertEquals("(((\"test\" bio_name:test) OR lang:de) OR -(lang:en)) -(is:retweet)",
                 p.and(p2)
                  .capsule()
                  .or(p3)
                  .capsule()
                  .or(p4)
                  .capsule()
                  .and(FilteredStreamRulePredicate.isRetweet().negate())
                  .toString());
  }

}