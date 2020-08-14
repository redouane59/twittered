package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.TwitterClient;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.CustomLog;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@CustomLog
@AllArgsConstructor
public class RequestHelperV2 extends AbstractRequestHelper {

    public String bearerToken;

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
                LOGGER.info(()->"Error 401, user may be private");
                return Optional.empty();
            }
            LOGGER.info(()->stringResponse);
            result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
        return Optional.ofNullable(result);
    }
}
