package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Slf4j
@Getter
public abstract class AbstractRequestHelper {

    private int sleepTime = 5;

    public void wait(String response, String url){
        LOGGER.info("\n" + response +"\nWaiting ... " + url); // do a wait and return this function recursively
        try {
            TimeUnit.MINUTES.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public OkHttpClient getHttpClient(String url){
        long   cacheSize = 1024L * 1024 * 1024; // 1go
        String path      = "../okhttpCache";
        File   file      = new File(path);
        return new OkHttpClient.Builder()
            .addNetworkInterceptor(new CacheInterceptor(this.getCacheTimeoutFromUrl(url)))
            .cache(new Cache(file, cacheSize))
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();
    }

    private int getCacheTimeoutFromUrl(String url){
        int defaultCache = 48;
        URL cacheUrl = this.getClass().getClassLoader().getResource("cache-config.json");
        if(cacheUrl==null){
            LOGGER.error("cache-config.json file not found in src/main/resources");
            return defaultCache;
        }
        try {
            Map<String, Integer> map = TwitterClient.OBJECT_MAPPER.readValue(cacheUrl, Map.class);
            for(Map.Entry<String, Integer> e : map.entrySet()){
                if(url.contains(e.getKey())){
                    return e.getValue();
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return defaultCache;
    }
}
