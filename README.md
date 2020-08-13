This project is a JAVA library which allows you to consume the Twitter API.

[![v2](https://img.shields.io/endpoint?url=https%3A%2F%2Ftwbadges.glitch.me%2Fbadges%2Fv2)](https://developer.twitter.com/en/docs/twitter-api)

[![Standard](https://img.shields.io/static/v1?label=Twitter%20API&message=v1.1&color=794BC4&style=flat&logo=Twitter)](https://developer.twitter.com/en/docs/api-reference-index)

[![Premium](https://img.shields.io/static/v1?label=Twitter%20API&message=Premium&color=794BC4&style=flat&logo=Twitter)](https://developer.twitter.com/en/docs/tweets/search/api-reference/premium-search)


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

/!\ [JDK 12](https://jdk.java.net/12/) is needed to run the project /!\

### Available methods
See : 
- [ITwitterClient.java](https://github.com/redouane59/twitter-client/blob/master/src/main/java/com/github/redouane59/twitter/ITwitterClient.java)
- [ITweet.java](https://github.com/redouane59/twitter-client/blob/master/src/main/java/com/github/redouane59/twitter/dto/tweet/ITweet.java)
- [IUser.java](https://github.com/redouane59/twitter-client/blob/master/src/main/java/com/github/redouane59/twitter/dto/user/IUser.java)

### Contribution
If you want to contribute to the project, don't hesitate to submit pull requests.
To add a new feature :
- Create the interface method in the related interface (i.e [ITwitterClient.java](https://github.com/redouane59/twitter-client/blob/master/src/main/java/com/github/redouane59/twitter/ITwitterClient.java))
- If needed, add the endpoint URL in [URLHelper.java](https://github.com/redouane59/twitter-client/blob/master/src/main/java/com/github/redouane59/twitter/helpers/URLHelper.java)
- Implement your method in the child class (i.e [TwitterClient.java](https://github.com/redouane59/twitter-client/blob/master/src/main/java/com/github/redouane59/twitter/TwitterClient.java))
- Don't forget to add your unit tests in `src/test/java/com/github/redouane59/twitter/unit`

### External Resources
[Twitter Developers docs](https://developer.twitter.com/en/docs)

### Special thanks
[@hypr2771](https://github.com/hypr2771)
[@mmornati](https://github.com/mmornati)
[@andypiper](https://github.com/andypiper)
[@igorbrigadir](https://github.com/igorbrigadir)

