This project is a JAVA library which allows you to consume the Twitter API.

## /!\ Development in progress... Any feedback is welcome :) /!\ 

### Configuration
The file `src/main/resources/twitter-credentials.json` should contain your twitter credentials like this :
```
{
  "apiKey": "xxx",
  "apiSecretKey": "xxx",
  "accessToken": "xxx",
  "accessTokenSecret": "xxx"
}
```
They can be found in [your twitter app page](https://developer.twitter.com/en/apps) in the _Key and tokens_
tab.

### How to use it
1) Run `mvn clean install`
2) Add a dependency on your project with the generated `.jar` file from `target` folder
3) Start using `TwitterClient` class

### Available methods
See : 
- [ITwitterClient.java](https://github.com/redouane59/twitter-client/blob/master/src/main/java/com/socialmediaraiser/twitter/ITwitterClient.java)
- [ITweet.java](https://github.com/redouane59/twitter-client/blob/master/src/main/java/com/socialmediaraiser/twitter/dto/tweet/ITweet.java)
- [IUser.java](https://github.com/redouane59/twitter-client/blob/master/src/main/java/com/socialmediaraiser/twitter/dto/user/IUser.java)

### External Resources
[Twitter Developers docs](https://developer.twitter.com/en/docs)

### Special thanks
[@hypr2771](https://github.com/hypr2771)
[@mmornati](https://github.com/mmornati)
[@andypiper](https://github.com/andypiper)
[@igorbrigadir](https://github.com/igorbrigadir)

