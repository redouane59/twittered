This project is a JAVA library which allows you to consume the Twitter API.

### Configuration
The file `src/main/resources/twitter-credentials.json` should contain your twitter credentials like this :
```
{
  "consumerKey": "xxx",
  "consumerSecret": "xxx",
  "accessToken": "xxx",
  "secretToken": "xxx"
}
```
It can be found in [your app page](https://developer.twitter.com/en/apps) in the _Key and tokens_
tab.

### Available methods
#### User related
```
List<AbstractUser> getFollowerUsers(String userId);
List<String> getFollowerIds(String userId);
List<AbstractUser> getFollowingsUsers(String userId);
List<String> getFollowingIds(String userId);
Set<String> getUserFollowersIds(String userId);
RelationType getRelationType(String userId1, String userId2);
AbstractUser getUserFromUserName(String userName);
AbstractUser getUserFromUserId(String userId);
boolean follow(String userId);
boolean unfollow(String userId);
boolean unfollowByName(String userName);
List<Tweet> getUserLastTweets(String userId, int count);
``` 
#### Tweet related
```
void likeTweet(String tweetId);
void retweetTweet(String tweetId);
List<String> getRetweetersId(String tweetId);
```
### External Resources
[Twitter Developers docs](https://developer.twitter.com/en/docs)

### Special thanks
@hypr2771

