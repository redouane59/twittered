package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.TwitterClient;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Slf4j
@Getter
public abstract class AbstractRequestHelper {

  private       int          sleepTime  = 5;
  private final OkHttpClient httpClient = new OkHttpClient.Builder()
      .readTimeout(60, TimeUnit.SECONDS)
      .connectTimeout(60, TimeUnit.SECONDS)
      .build();

  public void wait(String response, String url) {
    LOGGER.info("\n" + response + "\nWaiting ... " + url); // do a wait and return this function recursively
    try {
      TimeUnit.MINUTES.sleep(sleepTime);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage(), e);
      Thread.currentThread().interrupt();
    }
  }

  public OkHttpClient getHttpClient(String url) {
    File configFile = new File("../cache-config.json");
    if (configFile.exists()) {
      File cacheFolder = new File("../okhttpCache");
      long cacheSize   = 1024L * 1024 * 1024; // 1go
      return new OkHttpClient.Builder()
          .addNetworkInterceptor(new CacheInterceptor(this.getCacheTimeoutFromUrl(url, configFile)))
          .cache(new Cache(cacheFolder, cacheSize))
          .readTimeout(60, TimeUnit.SECONDS)
          .connectTimeout(60, TimeUnit.SECONDS)
          .build();
    } else {
      return this.httpClient;
    }
  }

  public static void logApiError(String method, String url, String stringResponse, int code) {
    LOGGER.error("(" + method + ") Error calling " + url + " " + stringResponse + " - " + code);
  }

  private int getCacheTimeoutFromUrl(String url, File configFile) {
    int defaultCache = 48;
    try {
      Map<String, Integer> map = TwitterClient.OBJECT_MAPPER.readValue(configFile, Map.class);
      for (Map.Entry<String, Integer> e : map.entrySet()) {
        if (url.contains(e.getKey())) {
          return e.getValue();
        }
      }
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return defaultCache;
  }
}
