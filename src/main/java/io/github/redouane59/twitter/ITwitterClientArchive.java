package io.github.redouane59.twitter;

import io.github.redouane59.twitter.dto.tweet.TweetV1;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ITwitterClientArchive {

  /**
   * Parse the Twitter extract data archive file in a List of Tweets
   *
   * @param file the downloaded file on https://twitter.com/settings/your_twitter_data converted in .json format
   * @return the list of tweets
   * @throws IOException if excpetion when reading file
   */
  List<TweetV1> readTwitterDataFile(File file) throws IOException;

}

