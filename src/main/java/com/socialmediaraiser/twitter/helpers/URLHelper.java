package com.socialmediaraiser.twitter.helpers;

import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@CustomLog
public class URLHelper {

    private static final String ROOT_URL = "https://api.twitter.com/1.1";
    private static final String ROOT_URL_V2 = "https://api.twitter.com/labs/2";
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
    public static final String LAST_TWEET_LIST_URL = ROOT_URL + STATUSES + USER_TIMELINE;
    public static final String RATE_LIMIT_URL = ROOT_URL + "/application/rate_limit_status.json";;
    public static final String SEARCH_TWEET_30_DAYS_URL = ROOT_URL + TWEETS + SEARCH + THIRTY_DAYS + DEV_ENV_NAME + JSON;
    public static final String SEARCH_TWEET_FULL_ARCHIVE_URL = ROOT_URL + TWEETS + SEARCH + FULL_ARCHIVE + DEV_ENV_NAME + JSON;
    public static final String SEARCH_TWEET_STANDARD_URL = ROOT_URL + SEARCH + TWEETS + JSON;
    public static final String LIVE_EVENT_URL = ROOT_URL + ACCOUNT_ACTIVITY + DEV_ENV_NAME + WEBHOOKS + JSON;
    public static final String SEARCH_TWEET_7_DAYS_URL = ROOT_URL_V2+TWEETS+SEARCH;
    public static final String GET_BEARER_TOKEN_URL = "https://api.twitter.com/oauth2/token";
    public static final String GET_OAUTH1_TOKEN_URL = "https://api.twitter.com/oauth/request_token";


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

    public String getUserUrl(String userId) {
        return new StringBuilder(ROOT_URL_V2)
                .append(USERS)
                .append("/")
                .append(userId)
                .append("?user.fields=")
                .append("id,created_at,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected")
                .toString();
    }

    public String getUserUrlFromName(String username) {
        return new StringBuilder(ROOT_URL_V2)
                .append(USERS)
                .append("/by/username/")
                .append(username)
                .append("?user.fields=")
                .append("id,created_at,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected")
                .toString();
    }

    public String getTweetUrl(String tweetId){
        return new StringBuilder(ROOT_URL_V2)
                .append("/tweets/")
                .append(tweetId)
                .append("?expansions=author_id")
                .append("&tweet.fields=attachments,author_id,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,source,text,withheld")
                .append("&user.fields=id,created_at,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected")
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

    public String getLikeUrl(String tweetId) {
        return new StringBuilder(ROOT_URL)
                .append(FAVORITES)
                .append(CREATE_JSON)
                .append(ID+"=")
                .append(tweetId)
                .toString();
    }
}
