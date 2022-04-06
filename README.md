This project is a JAVA library which allows you to consume the Twitter API.

[![v2](https://img.shields.io/endpoint?url=https%3A%2F%2Ftwbadges.glitch.me%2Fbadges%2Fv2)](https://developer.twitter.com/en/docs/twitter-api)

[![Standard](https://img.shields.io/static/v1?label=Twitter%20API&message=v1.1&color=794BC4&style=flat&logo=Twitter)](https://developer.twitter.com/en/docs/api-reference-index)

[![Premium](https://img.shields.io/static/v1?label=Twitter%20API&message=Premium&color=794BC4&style=flat&logo=Twitter)](https://developer.twitter.com/en/docs/tweets/search/api-reference/premium-search)

### Configuration

![Maven Central](https://img.shields.io/maven-central/v/io.github.redouane59.twitter/twittered)

In your pom.xml, add the following dependency and replace `VERSION` with the version you wish:

```xml
<dependency>
  <groupId>io.github.redouane59.twitter</groupId>
  <artifactId>twittered</artifactId>
  <version>VERSION</version>
</dependency>
```

If you are using Gradle Kotlin DSL, make sure you have MavenCentral among the available repositories:
```kotlin
repositories {
    mavenCentral()
    // [...]
}
```
Then add the following line to your `dependencies` block:

```kotlin
implementation("io.github.redouane59.twitter:twittered:VERSION")
```

To be able to see library logs, also add sl4j references :

```xml
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
  <version>1.7.30</version>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-simple</artifactId>
  <version>1.7.30</version>
</dependency>
``` 

In order to use your own developer credentials, you have several options :

#### Using a json file

File example :

```json
{
  "apiKey": "xxx",
  "apiSecretKey": "xxx",
  "accessToken": "xxx",
  "accessTokenSecret": "xxx"
}
```

##### With program argument

Pass through java argument your file path like `-Dtwitter.credentials.file.path=/your/path/to/json`
. Then instantiate the client like

```java
TwitterClient client = new TwitterClient();
```

or

##### Using deserialization in your code

```java
TwitterClient twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                                    .readValue(new File("/your/path/to/json"), TwitterCredentials.class));
``` 

#### With hard-coded values

```java
TwitterClient twitterClient = new TwitterClient(TwitterCredentials.builder()
                                                            .accessToken("<access_token>")
                                                            .accessTokenSecret("<secret_token>")
                                                            .apiKey("<api_key>")
                                                            .apiSecretKey("<secret_key>")
                                                            .build());
``` 

NB : Your twitter credentials can be found
in [your twitter app page](https://developer.twitter.com/en/apps) in the _Key and tokens_
page.

### Available methods

See :

- [ITwitterClientV2.java](https://github.com/redouane59/twittered/blob/develop/src/main/java/io/github/redouane59/twitter/ITwitterClientV2.java)
- [ITwitterClientV1.java](https://github.com/redouane59/twittered/blob/develop/src/main/java/io/github/redouane59/twitter/ITwitterClientV1.java)
- [Tweet.java](https://github.com/redouane59/twittered/blob/develop/src/main/java/io/github/redouane59/twitter/dto/tweet/Tweet.java)
- [User.java](https://github.com/redouane59/twittered/blob/develop/src/main/java/io/github/redouane59/twitter/dto/user/User.java)

### Code samples

See :

- [ITwitterClientV2Test.java](https://github.com/redouane59/twittered/blob/develop/src/test/java/io/github/redouane59/twitter/nrt/ITwitterClientV2Test.java)
- [ITwitterClientV2AuthenticatedTest.java](https://github.com/redouane59/twittered/blob/develop/src/test/java/io/github/redouane59/twitter/nrt/ITwitterClientV2AuthenticatedTest.java)
- [ITwitterClientV1Test.java](https://github.com/redouane59/twittered/blob/develop/src/test/java/io/github/redouane59/twitter/nrt/ITwitterClientV1Test.java)

### Basic examples

#### 1. Init TwitterClient

```java
TwitterClient twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                  .readValue(new File("/your/path/to/json"), TwitterCredentials.class));
```

#### 2. Get Tweet object from tweet id and display some information

```java
Tweet  tweet   = twitterClient.getTweet("1224041905333379073");
System.out.println(tweet.getText());
System.out.println(tweet.getCreatedAt());
System.out.println(tweet.getLang());
System.out.println(tweet.getLikeCount());
System.out.println(tweet.getRetweetCount());
System.out.println(tweet.getReplyCount());
System.out.println(tweet.getUser().getName());
```

#### 3. Get User object from username and display some information

```java
User   user   = twitterClient.getUserFromUserName("RedouaneBali");
System.out.println(tweet.getUser().getName());
System.out.println(tweet.getUser().getDisplayedName());
System.out.println(tweet.getUser().getDateOfCreation());
System.out.println(tweet.getUser().getDescription());
System.out.println(tweet.getUser().getTweetCount());
System.out.println(tweet.getUser().getFollowersCount());
System.out.println(tweet.getUser().getFollowingCount());
System.out.println(tweet.getUser().getPinnedTweet());
System.out.println(tweet.getUser().getPinnedTweet());
System.out.println(tweet.getUser().getLocation());
System.out.println(tweet.getUser().getId());
System.out.println(tweet.getUser().getUrl());
```

### Contribution

If you want to contribute to the project, don't hesitate to submit pull requests. To add a new
feature :

- Create the interface method in the related interface (
  e.g [ITwitterClientV2.java](https://github.com/redouane59/twittered/blob/develop/src/main/java/io/github/redouane59/twitter/ITwitterClientV2.java))
- If needed, add the endpoint URL
  in [URLHelper.java](https://github.com/redouane59/twittered/blob/develop/src/main/java/io/github/redouane59/twitter/helpers/URLHelper.java)
- Implement your method in the child class (
  e.g [TwitterClient.java](https://github.com/redouane59/twittered/blob/develop/src/main/java/io/github/redouane59/twitter/TwitterClient.java))
- Don't forget to add your unit tests in `src/test/java/io/github/redouane59/twitter/unit`

Code style is also available in `.idea/codeStyles/GoogleStyle.xml` file.

### External Resources

[Twitter Developers docs](https://developer.twitter.com/en/docs)

### Special thanks

I would like to give special thanks to
[@hypr2771](https://github.com/hypr2771)
[@mmornati](https://github.com/mmornati)
[@andypiper](https://github.com/andypiper)
[@igorbrigadir](https://github.com/igorbrigadir)
[@sparack](https://github.com/sparack)
for having helped me building this Twitter API library for Java. The tool is now working and I hope
that students, junior and senior developers will enjoy it, being able to play easily with twitter
data using Java programming language.
