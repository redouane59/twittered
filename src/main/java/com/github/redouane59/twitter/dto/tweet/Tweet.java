package com.github.redouane59.twitter.dto.tweet;

import com.github.redouane59.twitter.dto.user.User;
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
}
