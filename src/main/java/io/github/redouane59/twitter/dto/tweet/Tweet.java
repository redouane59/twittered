package io.github.redouane59.twitter.dto.tweet;

import io.github.redouane59.twitter.dto.stream.StreamRules;
import io.github.redouane59.twitter.dto.tweet.TweetV2.Place;
import io.github.redouane59.twitter.dto.tweet.entities.Entities;
import io.github.redouane59.twitter.dto.tweet.entities.MediaEntity;
import io.github.redouane59.twitter.dto.user.User;
import java.time.LocalDateTime;
import java.util.List;

public interface Tweet {

  /**
   * Get the id of the tweet
   *
   * @return the id of the tweet
   */
  String getId();

  /**
   * Get the text of the tweet
   *
   * @return the text of the tweet
   */
  String getText();

  /**
   * Get the user of the tweet. Warning : in some cases, the tweet doesn't contain this value
   *
   * @return the author user object
   */
  User getUser();

  /**
   * Get the id of the tweet author
   *
   * @return the author id
   */
  String getAuthorId();

  /**
   * Get the number of retweets of the tweet
   *
   * @return the number of the tweet retweets
   */
  int getRetweetCount();

  /**
   * Get the number of likes of the tweet
   *
   * @return the number of the tweet likes
   */
  int getLikeCount();

  /**
   * Get the number of replies of the tweet
   *
   * @return the number of the tweet replies
   */
  int getReplyCount();

  /**
   * Get the number of quotes of the tweet
   *
   * @return the number of the tweet quotes
   */
  int getQuoteCount();

  /**
   * Get the number of times the Tweet has been viewed. This is a private metric, and requires the
   * use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   *
   * @return the number of times the Tweet has been viewed.
   */
  int getImpressionCount();

  /**
   * Get the number of times a user clicks on a URL link or URL preview card in a Tweet. This is a
   * private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   *
   * @return the number of times a user clicks on a URL link or URL preview card in a Tweet.
   */
  int getUrlLinkClicks();

  /**
   * Get the number of times a user clicks the following portions of a Tweet - display name, user
   * name, profile picture. This is a private metric, and requires the use of OAuth 1.0a or OAuth
   * 2.0 User Context authentication.
   * 
   * API v2 only!
   *
   * @return the number of times a user clicks the following portions of a Tweet - display name,
   *         user name, profile picture.
   */
  int getUserProfileClicks();

  /**
   * Get the number of times the Tweet has been viewed organically. This is a private metric, and
   * requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   * 
   * @return the number of times the Tweet has been viewed organically.
   */
  int getOrganicImpressionCount();

  /**
   * Get the number of likes the Tweet has received organically. This is a private metric, and
   * requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   * 
   * @return the number of likes the Tweet has received organically.
   */
  int getOrganicLikeCount();

  /**
   * Get the number of replies the Tweet has received organically. This is a private metric, and
   * requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   *
   * API v2 only!
   * 
   * @return the number of replies the Tweet has received organically.
   */
  int getOrganicReplyCount();

  /**
   * Get the number of times the Tweet has been Retweeted organically. This is a private metric, and
   * requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   * 
   * @return the number of times the Tweet has been Retweeted organically. This is a private metric,
   *         and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   */
  int getOrganicRetweetCount();

  /**
   * Get the number of times a user clicks on a URL link or URL preview card in a Tweet organically.
   * This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context
   * authentication.
   * 
   * API v2 only!
   * 
   * @return the number of times a user clicks on a URL link or URL preview card in a Tweet
   *         organically.
   */
  int getOrganicUrlLinkClicks();

  /**
   * Get the number of times a user clicks the following portions of a Tweet organically - display
   * name, user name, profile picture. This is a private metric, and requires the use of OAuth 1.0a
   * or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   * 
   * @return the number of times a user clicks the following portions of a Tweet organically -
   *         display name, user name, profile picture.
   */
  int getOrganicUserProfileClicks();

  /**
   * Number of times the Tweet has been viewed when that Tweet is being promoted. This is a private
   * metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   * 
   * @return
   */
  int getPromotedImpressionCount();

  /**
   * Number of Likes of this Tweet when that Tweet is being promoted. This is a private metric, and
   * requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   * 
   * @return
   */
  int getPromotedLikeCount();

  /**
   * Number of Replies to this Tweet when that Tweet is being promoted. This is a private metric,
   * and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   * 
   * @return
   */
  int getPromotedReplyCount();

  /**
   * Number of times this Tweet has been Retweeted when that Tweet is being promoted. This is a
   * private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   * 
   * @return
   */
  int getPromotedRetweetCount();

  /**
   * Number of times a user clicks on a URL link or URL preview card in a Tweet when it is being
   * promoted. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User
   * Context authentication.
   * 
   * API v2 only!
   * 
   * @return
   */
  int getPromotedUrlLinkClicks();

  /**
   * Number of times a user clicks the following portions of a Tweet when it is being promoted -
   * display name, user name, profile picture. This is a private metric, and requires the use of
   * OAuth 1.0a or OAuth 2.0 User Context authentication.
   * 
   * API v2 only!
   * 
   * @return
   */
  int getPromotedUserProfileClicks();

  /**
   * Get the creation date of the tweet
   *
   * @return the tweet creation date
   */
  LocalDateTime getCreatedAt();

  /**
   * Get the language of the tweet
   *
   * @return the tweet language
   */
  String getLang();

  /**
   * Get the id of the user from whom the tweet is replying. Can be null if the tweet is not a reply.
   *
   * @return the user id of the initial tweet
   */
  String getInReplyToUserId();

  /**
   * Get the id of the tweet from whom the tweet is replying. Can be null if the tweet is not a reply.
   *
   * @return the tweet id of the initial tweet.
   */
  String getInReplyToStatusId();

  /**
   * Get the id of the tweet from whom the tweet is replying. Can be null if the tweet is not a reply.
   *
   * @param type the type of the tweet if he contains several elements like quoted, retweeted
   * @return the tweet id of the initial tweet.
   */
  String getInReplyToStatusId(TweetType type);

  /**
   * Get the context annotations of the tweet
   *
   * @return a list of ContextAnnotations
   */
  List<ContextAnnotation> getContextAnnotations();

  /**
   * Get the type of the tweet
   *
   * @return the tweet type
   */
  TweetType getTweetType();

  /**
   * Get the related tweet id
   *
   * @return the id of the first tweet of the conversation
   */
  String getConversationId();

  /**
   * Get the reply settings of the tweet
   */
  ReplySettings getReplySettings();

  /**
   * Get the geolocalisation of the tweet
   */
  Geo getGeo();

  /**
   * Get the attachments of the tweet
   */
  Attachments getAttachments();

  /**
   * Get the source label of the tweet
   */
  String getSource();

  /**
   * Get the entities of the tweet
   */
  Entities getEntities();

  /**
   * Get the {@link MediaEntity media entities} of the tweet
   */
  List<? extends MediaEntity> getMedia();

  /**
   * Get the {@link Place place} of the tweet
   */
  List<Place> getPlaces();

  /**
   * When an activity is delivered through a filtered stream connection, the matching_rules list contains which list of filters matched against the
   * Tweet delivered.
   */
  List<StreamRules.StreamRule> getMatchingRules();


}
