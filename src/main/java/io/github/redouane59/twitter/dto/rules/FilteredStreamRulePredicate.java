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

  private FilteredStreamRulePredicate(String predicate) {
    this.predicate = predicate;
  }

  private static FilteredStreamRulePredicate build(String one, String two, String operator) {
    FilteredStreamRulePredicate p = new FilteredStreamRulePredicate();
    if (one == null || one.isEmpty()) {
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
    return new FilteredStreamRulePredicate(emoji);
  }

  public static FilteredStreamRulePredicate withExactPhrase(String phrase) {
    return new FilteredStreamRulePredicate(quote(phrase));
  }

  public static FilteredStreamRulePredicate withHashtag(String hashtag) {
    String predicate = hashtag.startsWith("#") ? hashtag : "#" + hashtag;
    return new FilteredStreamRulePredicate(predicate);
  }

  public static FilteredStreamRulePredicate withMention(String mention) {
    String predicate = mention.startsWith("@") ? mention : "@" + mention;
    return new FilteredStreamRulePredicate(predicate);
  }

  public static FilteredStreamRulePredicate withCashtag(String cashtag) {
    String predicate = cashtag.startsWith("$") ? cashtag : "$" + cashtag;
    return new FilteredStreamRulePredicate(predicate);
  }

  public static FilteredStreamRulePredicate withUser(String user) {
    return new FilteredStreamRulePredicate("from:" + user);
  }

  public static FilteredStreamRulePredicate withReplyTo(String user) {
    return new FilteredStreamRulePredicate("to:" + user);
  }

  public static FilteredStreamRulePredicate withUrl(String url) {
    return new FilteredStreamRulePredicate("url:" + quote(url));
  }

  public static FilteredStreamRulePredicate withRetweetsOf(String user) {
    return new FilteredStreamRulePredicate("retweets_of:" + user);
  }

  public static FilteredStreamRulePredicate withContext(String context) {
    return new FilteredStreamRulePredicate("context:" + context);
  }

  public static FilteredStreamRulePredicate withEntity(String entity) {
    return new FilteredStreamRulePredicate("entity:" + quote(entity));
  }

  public static FilteredStreamRulePredicate withConversationId(String conversationId) {
    return new FilteredStreamRulePredicate("conversation_id:" + conversationId);
  }

  public static FilteredStreamRulePredicate withBio(String bio) {
    return new FilteredStreamRulePredicate("bio:" + bio);
  }

  public static FilteredStreamRulePredicate withBioName(String bioName) {
    return new FilteredStreamRulePredicate("bio_name:" + bioName);
  }

  public static FilteredStreamRulePredicate withBioLocation(String bioLocation) {
    return new FilteredStreamRulePredicate("bio_location:" + bioLocation);
  }

  public static FilteredStreamRulePredicate withPlace(String place) {
    return new FilteredStreamRulePredicate("place:" + place);
  }

  public static FilteredStreamRulePredicate withPlaceCountry(String alpha2IsoCode) {
    return new FilteredStreamRulePredicate("place_country:" + alpha2IsoCode);
  }

  public static FilteredStreamRulePredicate withPointRadius(double longitude, double latitude, String radius) {
    //mi or km
    String pointRadius = "[" + longitude + " " + latitude + " " + radius + "]";
    return new FilteredStreamRulePredicate("point_radius:" + pointRadius);
  }

  public static FilteredStreamRulePredicate withBoundingBox(double westLongitude, double southLatitude, double eastLongitude, double northLatitude) {
    //mi or km
    String boundingBox = "[" + westLongitude + " " + southLatitude + " " + eastLongitude + " " + northLatitude + "]";
    return new FilteredStreamRulePredicate("bounding_box:" + boundingBox);
  }

  public static FilteredStreamRulePredicate withLanguage(String language) {
    return new FilteredStreamRulePredicate("lang:" + language);
  }

  public static FilteredStreamRulePredicate isRetweet() {
    return new FilteredStreamRulePredicate("is:retweet");
  }

  public static FilteredStreamRulePredicate isReply() {
    return new FilteredStreamRulePredicate("is:reply");
  }

  public static FilteredStreamRulePredicate isQuote() {
    return new FilteredStreamRulePredicate("is:quote");
  }

  public static FilteredStreamRulePredicate isVerified() {
    return new FilteredStreamRulePredicate("is:verified");
  }

  public static FilteredStreamRulePredicate isNullcast() {
    return new FilteredStreamRulePredicate("-is:nullcast");
  }

  public static FilteredStreamRulePredicate hasHashtags() {
    return new FilteredStreamRulePredicate("has:hashtags");
  }

  public static FilteredStreamRulePredicate hasCashtags() {
    return new FilteredStreamRulePredicate("has:cashtags");
  }

  public static FilteredStreamRulePredicate hasLinks() {
    return new FilteredStreamRulePredicate("has:links");
  }

  public static FilteredStreamRulePredicate hasMentions() {
    return new FilteredStreamRulePredicate("has:mentions");
  }

  public static FilteredStreamRulePredicate hasMedia() {
    return new FilteredStreamRulePredicate("has:media");
  }

  public static FilteredStreamRulePredicate hasImages() {
    return new FilteredStreamRulePredicate("has:images");
  }

  public static FilteredStreamRulePredicate hasVideos() {
    return new FilteredStreamRulePredicate("has:videos");
  }

  public static FilteredStreamRulePredicate hasGeo(String geo) {
    return new FilteredStreamRulePredicate("has:geo " + geo);
  }

  public static FilteredStreamRulePredicate doSampling(int sampleSize) {
    return new FilteredStreamRulePredicate("sample:" + sampleSize);
  }

  public FilteredStreamRulePredicate negate() {
    if (predicate == null) {
      throw new RuleBuilderException("Cannot negate empty predicate");
    }
    predicate = "-" + capsule();
    return this;
  }

  public FilteredStreamRulePredicate capsule() {
    if (predicate != null) {
      predicate = "(" + predicate + ")";
    }
    return this;
  }

  public FilteredStreamRulePredicate or(FilteredStreamRulePredicate other) {
    applyOperator(" OR ", other);
    return this;
  }

  public FilteredStreamRulePredicate and(FilteredStreamRulePredicate other) {
    applyOperator(" ", other);
    return this;
  }

  public static FilteredStreamRulePredicate empty() {
    return new FilteredStreamRulePredicate(null);
  }

  public boolean isEmpty() {
    return predicate == null;
  }

  private void applyOperator(String operator, FilteredStreamRulePredicate right) {
    if (this.predicate == null) {
      this.predicate = right.predicate;
    } else if (right != null && !right.isEmpty()) {
      this.predicate = this.predicate + operator + right.predicate;
    }
  }

  private static String quote(String phrase) {
    return "\"" + phrase + "\"";
  }

  @Override
  public String toString() {
    return (predicate != null ? predicate : "");
  }

  public static class RuleBuilderException extends RuntimeException {

    public RuleBuilderException(String message) {
      super(message);
    }
  }

}
