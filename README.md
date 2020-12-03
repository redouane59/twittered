This project is a JAVA library which allows you to consume the Twitter API.

[![v2](https://img.shields.io/endpoint?url=https%3A%2F%2Ftwbadges.glitch.me%2Fbadges%2Fv2)](https://developer.twitter.com/en/docs/twitter-api)

[![Standard](https://img.shields.io/static/v1?label=Twitter%20API&message=v1.1&color=794BC4&style=flat&logo=Twitter)](https://developer.twitter.com/en/docs/api-reference-index)

[![Premium](https://img.shields.io/static/v1?label=Twitter%20API&message=Premium&color=794BC4&style=flat&logo=Twitter)](https://developer.twitter.com/en/docs/tweets/search/api-reference/premium-search)

## /!\ Development in progress... Any [feedback](https://github.com/redouane59/twittered/issues/new/choose) is welcome :) /!\ 

### Configuration

In your pom.xml, add the following dependency :
```
<dependency>
  <groupId>com.github.redouane59.twitter</groupId>
  <artifactId>twittered</artifactId>
  <version>1.13</version>
</dependency>
```
In order to use your own developer credentials, you have several options :

#### Using a json file

File example :
```
{
  "apiKey": "xxx",
  "apiSecretKey": "xxx",
  "accessToken": "xxx",
  "accessTokenSecret": "xxx"
}
```

##### With program argument

Pass through java argument your file path like `-Dtwitter.credentials.file.path=/your/path/to/json` .
Then instantiate the client like 
```
TwitterClient client = new TwitterClient();
```

or 

##### Using deserialization in your code
```
TwitterClient twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                                    .readValue(new File("/your/path/to/json"), TwitterCredentials.class));
``` 
#### With hard-coded values
```
TwitterClient twitterClient = new TwitterClient(TwitterCredentials.builder()
                                                            .accessToken("<access_token>")
                                                            .accessTokenSecret("<secret_token>")
                                                            .apiKey("<api_key>")
                                                            .apiSecretKey("<secret_key>")
                                                            .build());
``` 

NB : Your twitter credentials can be found in [your twitter app page](https://developer.twitter.com/en/apps) in the _Key and tokens_
page. 

### Available methods
See : 
- [ITwitterClientV2.java](https://github.com/redouane59/twittered/blob/master/src/main/java/com/github/redouane59/twitter/ITwitterClientV2.java)
- [ITwitterClientV1.java](https://github.com/redouane59/twittered/blob/master/src/main/java/com/github/redouane59/twitter/ITwitterClientV1.java)
- [Tweet.java](https://github.com/redouane59/twittered/blob/master/src/main/java/com/github/redouane59/twitter/dto/tweet/Tweet.java)
- [User.java](https://github.com/redouane59/twittered/blob/master/src/main/java/com/github/redouane59/twitter/dto/user/User.java)

### Code samples
See : 
- [ITwitterClientV2Test.java](https://github.com/redouane59/twittered/blob/master/src/test/java/com/github/redouane59/twitter/nrt/ITwitterClientV2Test.java)
- [ITwitterClientV1Test.java](https://github.com/redouane59/twittered/blob/master/src/test/java/com/github/redouane59/twitter/nrt/ITwitterClientV1Test.java)

### Contribution
If you want to contribute to the project, don't hesitate to submit pull requests.
To add a new feature :
- Create the interface method in the related interface (e.g [ITwitterClientV2.java](https://github.com/redouane59/twittered/blob/master/src/main/java/com/github/redouane59/twitter/ITwitterClientV2.java))
- If needed, add the endpoint URL in [URLHelper.java](https://github.com/redouane59/twittered/blob/master/src/main/java/com/github/redouane59/twitter/helpers/URLHelper.java)
- Implement your method in the child class (e.g [TwitterClient.java](https://github.com/redouane59/twittered/blob/master/src/main/java/com/github/redouane59/twitter/TwitterClient.java))
- Don't forget to add your unit tests in `src/test/java/com/github/redouane59/twitter/unit`

Code style is also available in `.idea/codeStyles/GoogleStyle.xml` file.

/!\ [JDK 12](https://jdk.java.net/12/) is needed to run the project /!\

### External Resources
[Twitter Developers docs](https://developer.twitter.com/en/docs)

### Special thanks
[@hypr2771](https://github.com/hypr2771)
[@mmornati](https://github.com/mmornati)
[@andypiper](https://github.com/andypiper)
[@igorbrigadir](https://github.com/igorbrigadir)
[@sparack](https://github.com/sparack)

