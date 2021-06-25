package io.github.redouane59.twitter.dto.collections;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.dto.tweet.TweetV1;
import io.github.redouane59.twitter.dto.user.UserV1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * See Collections <a href ="https://developer.twitter.com/en/docs/twitter-api/v1/tweets/curate-a-collection/overview/response_structures"> Response
 * Structures</a>
 */
@Getter
@Setter
public class CollectionsResponse {

  /**
   * Populated if  destroy collection is called - https://api.twitter.com/1.1/collections/destroy.json
   */
  private boolean destroyed;

  @JsonProperty("response")
  private Response response;

  @JsonProperty("objects")
  private Objects objects;

  public boolean hasErrors() {
    return response != null && response.errors != null && !response.errors.isEmpty();
  }

  @Getter
  @Setter
  public static class Response {

    /**
     * The timeline_id is the collection identifier.
     */
    @JsonProperty("timeline_id")
    private String timeLineId;

    /**
     * Errors reported from call to curate a collection - empty list indicates success Note: response to create collection will not populate this
     */
    private List<Error> errors = new ArrayList<>();

    private List<TimelineTweet> timeline = new ArrayList<>();


    private Position position;


    @Getter
    @Setter
    public static class Position {

      @JsonProperty("max_position")
      private String maxPosition;

      @JsonProperty("min_position")
      private String minPosition;

      /**
       * was_truncated indicates whether additional Tweets exist in the collection outside of the range of the current request
       */
      @JsonProperty("was_truncated")
      private Boolean wasTruncated;
    }


    @Getter
    @Setter
    public static class TimelineTweet {

      private Value tweet;

      @JsonProperty("feature_context")
      private String featureContext;


      @Getter
      @Setter
      public static class Value {

        private String id;

        @JsonProperty("sort_index")
        private String sortIndex;

      }
    }

    @Getter
    @Setter
    public static class Error {

      /**
       * Reason the tweet was rejected when added to collection
       */
      private String reason;
      /**
       * The change that was requested i.e. 'add' tweet to collection
       */
      private Change change;

      @Getter
      @Setter
      public static class Change {

        @JsonProperty("op")
        private String operation;
        @JsonProperty("tweet_id")
        private String tweetId;

      }

    }

  }

  @Getter
  @Setter
  public static class Objects {

    private Tweets    tweets;
    private Users     users;
    private Timelines timelines;


    @Getter
    @Setter
    public static class Tweets {

      // TweetId Mapped to the tweet itself
      private Map<String, TweetV1> tweetDetails = new HashMap<>();

      @JsonAnyGetter
      public Map<String, TweetV1> getTweetDetails() {
        return tweetDetails;
      }

      @JsonAnySetter
      public void setTweetDetails(String name, TweetV1 value) {
        this.tweetDetails.put(name, value);
      }

      public List<TweetV1> get() {
        return new ArrayList<>(tweetDetails.values());
      }
    }

    @Getter
    @Setter
    public static class Users {

      // UserId Mapped to the tweet itself
      private Map<String, UserV1> userDetails = new HashMap<>();

      @JsonAnyGetter
      public Map<String, UserV1> getUserDetails() {
        return userDetails;
      }

      @JsonAnySetter
      public void setUserDetails(String name, UserV1 value) {
        this.userDetails.put(name, value);
      }

      public List<UserV1> get() {
        return new ArrayList<>(userDetails.values());
      }


    }

    @Getter
    @Setter
    public static class Timelines {

      // Timeline id Mapped to the timeline itself
      private Map<String, Timeline> timelineDetails = new HashMap<>();

      @JsonAnyGetter
      public Map<String, Timeline> getTimelineDetailsDetails() {
        return timelineDetails;
      }

      @JsonAnySetter
      public void setTimelineDetails(String name, Timeline value) {
        this.timelineDetails.put(name, value);
      }

      public List<Timeline> get() {
        return new ArrayList<>(timelineDetails.values());
      }

      @Getter
      @Setter
      public static class Timeline {

        @JsonProperty("name")
        private String name;
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty("collection_url")
        private String collectionUrl;
        @JsonProperty("custom_timeline_url")
        private String collectionTimelineUrl;
        @JsonProperty("description")
        private String description;
        @JsonProperty("url")
        private String url;
        @JsonProperty("visibility")
        private String visibility;
        @JsonProperty("timeline_order")
        private String timelineOrder;
        @JsonProperty("collection_type")
        private String collectionType;
        @JsonProperty("custom_timeline_type")
        private String collectionTimelineType;
      }
    }
  }
}
