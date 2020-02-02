package com.socialmediaraiser.twitter.helpers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

public class JsonHelper {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Logger LOGGER = Logger.getLogger(JsonHelper.class.getName());
    private static final String NEXT_CURSOR = "next_cursor";
    public static final String FOLLOWING = "following";

    @Deprecated
    public Long getLongFromCursorObject(JsonNode response){
        if(response==null){
            LOGGER.severe(()->"result null");
            return null;
        }
        return response.get(NEXT_CURSOR).asLong();
    }

    // @todo in converterHelper ?
    public static Date getDateFromTwitterString(String date)
    {
        if(date==null) return null;
        final String TWITTER = "EEE MMM dd HH:mm:ss Z yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);
        return Try.of(() -> sf.parse(date)).getOrNull();
    }

    // @todo in converterHelper ?
    public static Date getDateFromTwitterDateV2(String date)
    {
        return Option.of(date).map(d -> Date.from(Instant.parse(date))).getOrNull();
    }

}
