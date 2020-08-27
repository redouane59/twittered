package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.TwitterClient;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@AllArgsConstructor
public class RequestHelperV2 extends AbstractRequestHelper {

    public String bearerToken;

    public <T> Optional<T> executeGetRequest(String url, Class<T> classType) {
        return this.executeGetRequestWithParameters(url, null, classType);
    }

    public <T> Optional<T> executeGetRequestWithParameters(String url, Map<String, String> parameters, Class<T> classType) {
        T result = null;
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
            if (parameters != null) {
                for(Map.Entry<String, String> param : parameters.entrySet()) {
                    httpBuilder.addQueryParameter(param.getKey(),param.getValue());
                }
            }
            Request request = new Request.Builder()
                .url(httpBuilder.build())
                .get()
                .headers(Headers.of("Authorization", "Bearer " + bearerToken))
                .build();
            String newUrl = httpBuilder.build().url().toString();
            Response response = this.getHttpClient(newUrl)
                                    .newCall(request).execute();
            String stringResponse = response.body().string();
            if (response.code()==429){
                this.wait(stringResponse, url);
                return this.executeGetRequestWithParameters(url, parameters, classType);
            } else if (response.code()==401){
                LOGGER.info("Error 401, user may be private");
                return Optional.empty();
            }
            LOGGER.info(stringResponse);
            result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    public <T> Optional<T> executePostRequest(String url, String body, Class<T> classType) {
        T result = null;
        try {
            Request request = new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(MediaType.parse("application/json"), body))
                .headers(Headers.of("Authorization", "Bearer " + bearerToken))
                .build();
            Response response = new OkHttpClient.Builder().build().newCall(request).execute();
            if(response.code()!=200){
                LOGGER.error("(POST) ! not 200 calling " + url + " " + response.message() + " - " + response.code());
            }
            String stringResponse = response.body().string();
            result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
        } catch(Exception e){
            LOGGER.error(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    public static <T> Optional<T> executePostRequestWithHeader(String url, Map<String, String> headersMap, String body, Class<T> classType) {
        T result = null;
        try {
            Request request = new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), body))
                .headers(Headers.of(headersMap))
                .build();
            Response response = new OkHttpClient.Builder().build().newCall(request).execute();
            if(response.code()!=200){
                LOGGER.error("(POST) ! not 200 calling " + url + " " + response.message() + " - " + response.code());
            }
            String stringResponse = response.body().string();
            result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
        } catch(Exception e){
            LOGGER.error(e.getMessage());
        }
        return Optional.ofNullable(result);
    }
}
