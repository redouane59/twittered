package com.socialmediaraiser.core.twitter.helpers;

import lombok.Data;

import java.util.List;
import java.util.logging.Logger;

@Data
public class URLHelper {

    private static final Logger LOGGER = Logger.getLogger(URLHelper.class.getName());

    private static final String ROOT_URL = "https://api.twitter.com/1.1";
    private static final String ROOT_URL_V2 = "https://api.twitter.com/labs/1";
    private static final String IDS_JSON = "/ids.json?";
    private static final String SCREEN_NAME = "screen_name";
    private static final String ID = "id";
    private static final String COUNT = "count";
    private static final String LIST_JSON = "/list.json?";
    private static final String SHOW_JSON = "/show.json?";
    private static final String CREATE_JSON = "/create.json?";
    private static final String DESTROY_JSON = "/destroy.json?";
    private static final String RETWEETERS = "/retweeters";
    private static final String FOLLOWERS = "/followers";
    private static final String FRIENDS = "/friends";
    private static final String STATUSES = "/statuses";
    private static final String FRIENDSHIPS = "/friendships";
    private static final String FAVORITES = "/favorites";
    private static final String USERS = "/users";
    private static final String TWEETS = "/tweets";
    private static final String SEARCH = "/search";
    private static final String THIRTY_DAYS = "/30day";
    private static final String FULL_ARCHIVE = "/fullarchive";
    private static final String DEV_ENV_NAME = "/dev"; // @todo config
    private static final String ACCOUNT_ACTIVITY = "/account_activity/all";
    private static final String WEBHOOKS = "/webhooks";
    private static final String USER_ID = "user_id";
    private static final String LOOKUP_JSON = "/lookup.json?";
    private static final String USER_TIMELINE = "/user_timeline.json?";
    private static final String JSON = ".json";
    private static final String TRIM_USER = "trim_user=true";
    private static final String EXCLUDE_RTS = "include_rts=false";
    private static final String USER_FORMAT_DETAILED= "user.format=detailed";
    private static final String TWEET_FORMAT_DETAILED= "tweet.format=detailed";
    private static final String EXPANSIONS_RECENT_TWEET= "expansions=most_recent_tweet_id";
    private static final int MAX_COUNT = 200;
    private static final int RETWEET_MAX_COUNT = 100;
    private static final int MAX_LOOKUP = 100;

    public String getFollowUrl(String userId) {
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(CREATE_JSON)
                .append(USER_ID+"=")
                .append(userId)
                .toString();
    }

    public String getUnfollowUrl(String userId) {
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(DESTROY_JSON)
                .append(USER_ID+"=")
                .append(userId)
                .toString();
    }

    public String getUnfollowByUsernameUrl(String userName) {
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(DESTROY_JSON)
                .append(SCREEN_NAME+"=")
                .append(userName)
                .toString();
    }

    public String getFriendshipUrl(String sourceId, String targetId) {
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(SHOW_JSON)
                .append("source_"+ ID +"=")
                .append(sourceId)
                .append("&target_"+ ID +"=")
                .append(targetId)
                .toString();
    }

    public String getRetweetersUrl(String tweetId){
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(RETWEETERS)
                .append(IDS_JSON)
                .append(ID +"=")
                .append(tweetId)
                .append("&"+COUNT+"=")
                .append(RETWEET_MAX_COUNT)
                .toString();
    }

    public String getFollowerIdsUrl(String userId){
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .toString();
    }


    public String getFollowerUsersUrl(String userId){
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(LIST_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .append("&"+COUNT+"=")
                .append(MAX_COUNT)
                .toString();
    }

    public String getFollowingIdsUrl(String userId){
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId).toString();
    }

    public String getFollowingUsersUrl(String userId){
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(LIST_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .append("&"+COUNT+"=")
                .append(MAX_COUNT)
                .toString();
    }

    public String getLastTweetListUrl(){
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(USER_TIMELINE).toString();
    }

    public String getUserUrl(String userId) {
        return new StringBuilder(ROOT_URL_V2)
                .append(USERS)
                .append("?ids=")
                .append(userId)
                .append("&"+USER_FORMAT_DETAILED)
                .append("&"+TWEET_FORMAT_DETAILED)
                .append("&"+EXPANSIONS_RECENT_TWEET)
                .toString();
    }

    public String getUserUrlFromName(String username) {
        return new StringBuilder(ROOT_URL_V2)
                .append(USERS)
                .append("?usernames=")
                .append(username)
                .append("&"+USER_FORMAT_DETAILED)
                .append("&"+TWEET_FORMAT_DETAILED)
                .append("&"+EXPANSIONS_RECENT_TWEET)
                .toString();
    }


    public String getUsersUrlbyNames(List<String> names) {
        StringBuilder result = new StringBuilder(ROOT_URL)
                .append(USERS)
                .append(LOOKUP_JSON)
                .append(SCREEN_NAME+"=");
        int i=0;
        while(i<names.size() && i<MAX_LOOKUP){
            String name = names.get(i);
            result.append(name);
            result.append(",");
            i++;
        }
        result.delete(result.length()-1,result.length());
        return result.toString();
    }

    public String getUsersUrlbyIds(List<String> ids) {
        StringBuilder result = new StringBuilder(ROOT_URL)
                .append(USERS)
                .append(LOOKUP_JSON)
                .append(USER_ID+"=");
        int i=0;
        while(i<ids.size() && i<MAX_LOOKUP){
            String id = ids.get(i);
            result.append(id);
            result.append(",");
            i++;
        }
        result.delete(result.length()-1,result.length());
        return result.toString();
    }

    public String getRateLimitUrl(){
        return new StringBuilder(ROOT_URL)
                .append("/application")
                .append("/rate_limit_status.json")
                .toString();
    }

    @Deprecated
    public String getTweetInfoUrl(String tweetId) {
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(SHOW_JSON)
                .append(ID+"=")
                .append(tweetId)
                .toString();
    }

    public String getUserTweetsUrl(String userId, int count){
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(USER_TIMELINE)
                .append(USER_ID+"=")
                .append(userId)
                .append("&"+COUNT+"=")
                .append(count)
                .append("&"+TRIM_USER)
                .append("&"+EXCLUDE_RTS)
                .toString();
    }

    public String getUserTweetsUrlV2(String userId, int count){
        throw new UnsupportedOperationException();
    }

    public String getSearchTweets30daysUrl() {
        return new StringBuilder(ROOT_URL)
                .append(TWEETS)
                .append(SEARCH)
                .append(THIRTY_DAYS)
                .append(DEV_ENV_NAME)
                .append(JSON)
                .toString();
    }

    public String getSearchTweetsUrlFull() {
        return new StringBuilder(ROOT_URL)
                .append(TWEETS)
                .append(SEARCH)
                .append(FULL_ARCHIVE)
                .append(DEV_ENV_NAME)
                .append(JSON)
                .toString();
    }

    public String getSearchTweetUrlStandard(){
        return new StringBuilder(ROOT_URL)
                .append(SEARCH)
                .append(TWEETS)
                .append(JSON)
                .toString();
    }


    public String getLiveEventUrl() {
        return new StringBuilder(ROOT_URL)
                .append(ACCOUNT_ACTIVITY)
                .append(DEV_ENV_NAME)
                .append(WEBHOOKS)
                .append(JSON)
                .toString();
    }

    public String getLikeUrl(String tweetId) {
        return new StringBuilder(ROOT_URL)
                .append(FAVORITES)
                .append(CREATE_JSON)
                .append(ID+"=")
                .append(tweetId)
                .toString();
    }
}
