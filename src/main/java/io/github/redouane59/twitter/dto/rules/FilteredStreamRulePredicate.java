package io.github.redouane59.twitter.dto.rules;

/**
 * This class brings support for enhanced filter rules for the filtered streaming API V2
 *
 * {@see <a href="https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule}">https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule}</a>
 */
public class FilteredStreamRulePredicate {

  private String predicate;

  FilteredStreamRulePredicate() {
  }

  private static FilteredStreamRulePredicate build(String one, String two, String operator) {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    if (one.isEmpty()) {
      p.predicate = operator + two;
    } else {
      p.predicate = one + " " + operator + two;
    }
    return p;
  }

  public static FilteredStreamRulePredicate withKeyword(String keyword) {
    return build("", keyword, "");
  }

  public static FilteredStreamRulePredicate withEmoji(String emoji) {
    return build("", emoji, "");
  }

  public static FilteredStreamRulePredicate withExactPhrase(String phrase) {
    return build("", "\"" + phrase + "\"", "");
  }

  public static FilteredStreamRulePredicate withHashtag(String hashtag) {
    return build("", hashtag.startsWith("#") ? hashtag : "#" + hashtag, "");
  }

  public static FilteredStreamRulePredicate withMention(String mention) {
    return build("", mention.startsWith("@") ? mention : "@" + mention, "");
  }

  public static FilteredStreamRulePredicate withCashtag(String cashtag) {
    return build("", cashtag.startsWith("$") ? cashtag : "$" + cashtag, "");
  }

  public static FilteredStreamRulePredicate withUser(String user) {
    return build("", user, "from:");
  }

  public static FilteredStreamRulePredicate withReplyTo(String user) {
    return build("", user, "to:");
  }

  public static FilteredStreamRulePredicate witUrl(String url) {
    return build("", withExactPhrase(url).toString(), "url:");
  }

  public static FilteredStreamRulePredicate withRetweetsOf(String user) {
    return build("", user, "retweets_of:");
  }

  public static FilteredStreamRulePredicate withContext(String context) {
    return build("", context, "context:");
  }

  public static FilteredStreamRulePredicate withEntity(String entity) {
    return build("", withExactPhrase(entity).toString(), "entity:");
  }

  public static FilteredStreamRulePredicate withConversationId(String conversationId) {
    return build("", conversationId, "conversation_id:");
  }

  public static FilteredStreamRulePredicate withBio(String bio) {
    return build("", bio, "bio:");
  }

  public static FilteredStreamRulePredicate withBioName(String bioName) {
    return build("", bioName, "bio_name:");
  }

  public static FilteredStreamRulePredicate withBioLocation(String bioLocation) {
    return build("", bioLocation, "bio_location:");
  }

  public static FilteredStreamRulePredicate withPlace(String place) {
    return build("", place, "place:");
  }

  public static FilteredStreamRulePredicate withPlaceCountry(String alpha2IsoCode) {
    return build("", alpha2IsoCode, "place_country:");
  }

  public static FilteredStreamRulePredicate withPointRadius(double longitude, double latitude, String radius) {
    //mi or km
    return build("", "[" + longitude + " " + latitude + " " + radius + "]", "point_radius:");
  }

  public static FilteredStreamRulePredicate withBoundingBox(double westLongitude, double southLatitude, double eastLongitude, double northLatitude) {
    //mi or km
    return build("", "[" + westLongitude + " " + southLatitude + " " + eastLongitude + " " + northLatitude + "]", "bounding_box:");
  }

  public static FilteredStreamRulePredicate withLanguage(String language) {
    return build("", language, "lang:");
  }

  public static FilteredStreamRulePredicate isRetweet(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "is:retweet");
  }

  public static FilteredStreamRulePredicate isReply(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "is:reply");
  }

  public static FilteredStreamRulePredicate isQuote(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "is:quote");
  }

  public static FilteredStreamRulePredicate isVerified(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "is:verified");
  }

  public static FilteredStreamRulePredicate isNullcast(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "-is:nullcast");
  }

  public static FilteredStreamRulePredicate hasHashtags(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "has:hashtags");
  }

  public static FilteredStreamRulePredicate hasCashtags(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "has:cashtags");
  }

  public static FilteredStreamRulePredicate hasLinks(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "has:links");
  }

  public static FilteredStreamRulePredicate hasMentions(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "has:mentions");
  }

  public static FilteredStreamRulePredicate hasMedia(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "has:media");
  }

  public static FilteredStreamRulePredicate hasImages(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "has:images");
  }

  public static FilteredStreamRulePredicate hasVideos(FilteredStreamRulePredicate predicate) {
    checkConjunction(predicate);
    return build(predicate.toString(), "", "has:videos");
  }

  public static FilteredStreamRulePredicate hasGeo(FilteredStreamRulePredicate predicate, String geo) {
    checkConjunction(predicate);
    return build(predicate.toString(), " " + geo, "has:geo");
  }

  public static FilteredStreamRulePredicate doSampling(FilteredStreamRulePredicate predicate, int sampleSize) {
    checkConjunction(predicate);
    return build(predicate.toString(), Integer.toString(sampleSize), "sample:");
  }

  private static void checkConjunction(FilteredStreamRulePredicate predicate) {
    if (predicate == null || predicate.isEmpty()) {
      throw new RuleBuilderException("Given operator can only be used in an conjunction");
    }
  }

  public FilteredStreamRulePredicate negate() {
    predicate = "-" + capsule();
    return this;
  }

  public FilteredStreamRulePredicate capsule() {
    predicate = "(" + predicate + ")";
    return this;
  }

  public FilteredStreamRulePredicate or(FilteredStreamRulePredicate other) {
    predicate += " OR " + other.predicate;
    return this;
  }

  public FilteredStreamRulePredicate and(FilteredStreamRulePredicate other) {
    predicate += " " + other.predicate;
    return this;
  }

  public boolean isEmpty() {
    return predicate == null || predicate.isEmpty();
  }

  @Override
  public String toString() {
    return predicate;
  }

  public static class RuleBuilderException extends RuntimeException {

    public RuleBuilderException(String message) {
      super(message);
    }
  }

}
