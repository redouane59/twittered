package io.github.redouane59.twitter.dto.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FilteredStreamRulePredicateTest {

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
                 FilteredStreamRulePredicate.witUrl("https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule")
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
  void testCapsule() {
    assertEquals("(point_radius:[2.355128 48.861118 16km])",
                 FilteredStreamRulePredicate.withPointRadius(2.355128, 48.861118, "16km").capsule().toString());
  }

  @Test
  void testIsRetweet() {
    assertEquals("test is:retweet", FilteredStreamRulePredicate.isRetweet(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testIsReply() {
    assertEquals("test is:reply", FilteredStreamRulePredicate.isReply(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testIsQuote() {
    assertEquals("test is:quote", FilteredStreamRulePredicate.isQuote(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testIsVerified() {
    assertEquals("test is:verified", FilteredStreamRulePredicate.isVerified(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testIsNullcast() {
    assertEquals("test -is:nullcast", FilteredStreamRulePredicate.isNullcast(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testHasHashtags() {
    assertEquals("test has:hashtags", FilteredStreamRulePredicate.hasHashtags(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testHasCashtags() {
    assertEquals("test has:cashtags", FilteredStreamRulePredicate.hasCashtags(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testHasLinks() {
    assertEquals("test has:links", FilteredStreamRulePredicate.hasLinks(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testHasMentions() {
    assertEquals("test has:mentions", FilteredStreamRulePredicate.hasMentions(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testHasMedia() {
    assertEquals("test has:media", FilteredStreamRulePredicate.hasMedia(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testHasImages() {
    assertEquals("test has:images", FilteredStreamRulePredicate.hasImages(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testHasVideos() {
    assertEquals("test has:videos", FilteredStreamRulePredicate.hasVideos(FilteredStreamRulePredicate.withKeyword("test")).toString());
  }

  @Test
  void testHasGeo() {
    assertEquals("test has:geo bakery", FilteredStreamRulePredicate.hasGeo(FilteredStreamRulePredicate.withKeyword("test"), "bakery").toString());
  }

  @Test
  void testSample() {
    assertEquals("test sample:15", FilteredStreamRulePredicate.doSampling(FilteredStreamRulePredicate.withKeyword("test"), 15).toString());
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
                 FilteredStreamRulePredicate.doSampling(p.and(p2).capsule().or(p3).capsule().or(p4), 15).toString());
  }

  @Test
  void testConjunctionOperatorsInvalid1() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isReply(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isReply(p);
    });

  }

  @Test
  void testConjunctionOperatorsInvalid2() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isRetweet(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isRetweet(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid3() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isQuote(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isQuote(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid4() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isVerified(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isVerified(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid5() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isNullcast(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.isNullcast(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid6() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasHashtags(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasHashtags(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid7() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasCashtags(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasCashtags(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid8() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasLinks(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasLinks(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid9() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasMentions(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasMentions(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid10() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasMedia(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasMedia(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid11() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasImages(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasImages(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid12() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasVideos(null);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasVideos(p);
    });
  }

  @Test
  void testConjunctionOperatorsInvalid13() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasGeo(null, "test");
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.hasGeo(p, "test");
    });
  }

  @Test
  void testConjunctionOperatorsInvalid14() {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.doSampling(null, 15);
    });
    Assertions.assertThrows(FilteredStreamRulePredicate.RuleBuilderException.class, () -> {
      FilteredStreamRulePredicate.doSampling(p, 15);
    });
  }
}