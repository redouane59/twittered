package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.TwitterClient;
import java.util.Map;
import java.util.Optional;
import lombok.CustomLog;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.utils.URIBuilder;

@CustomLog
public class RequestHelperV2 extends AbstractRequestHelper {

    public String bearerToken;

    public RequestHelperV2(String token){
        bearerToken = token;
    }

    public <T> Optional<T> executeGetRequestWithParameters(String url, Map<String, String> parameters, Class<T> classType) {
        T result = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            for(Map.Entry<String, String> e : parameters.entrySet()){
                builder.addParameter(e.getKey(), e.getValue());
            }
            Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(Headers.of("Authorization", "Bearer " + bearerToken))
                .build();

            Response response = this.getHttpClient(url)
                                    .newCall(request).execute();
            String stringResponse = response.body().toString();
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
