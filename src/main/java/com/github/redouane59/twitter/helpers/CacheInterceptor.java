package com.github.redouane59.twitter.helpers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {

  private int maxHours;

  public CacheInterceptor(int maxHours) {
    this.maxHours = maxHours;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());

    CacheControl cacheControl = new CacheControl.Builder()
        .maxAge(maxHours, TimeUnit.HOURS)
        .build();

    return response.newBuilder()
                   .removeHeader("Pragma")
                   .removeHeader("Cache-Control")
                   .header("Cache-Control", cacheControl.toString())
                   .build();
  }
}