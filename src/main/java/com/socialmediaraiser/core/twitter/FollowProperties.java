package com.socialmediaraiser.core.twitter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.socialmediaraiser.core.twitter.helpers.JsonHelper;
import com.socialmediaraiser.core.twitter.properties.*;
import lombok.Data;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Data
public class FollowProperties {

    private static final Logger LOGGER = Logger.getLogger(FollowProperties.class.getName());

    @Getter
    private static TwitterCredentials twitterCredentials;

    @Getter
    private static String arraySeparator = ",";

    private FollowProperties() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean load(String userName) {
        if(userName==null) return false;
        try {
           URL yamlFile = FollowProperties.class.getResource("/"+userName+".yaml");
            if(yamlFile==null){
                LOGGER.severe(()->"yaml file not found at /"+userName+".yaml");
                return false;
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            JsonHelper.OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            Map<String,Object> yaml = mapper.readValue(yamlFile, HashMap.class);
            twitterCredentials = JsonHelper.OBJECT_MAPPER.convertValue(yaml.get("twitter-credentials"), TwitterCredentials.class);
            LOGGER.info(()->"properties loaded correctly");
            return true;
        } catch (IOException ex) {
            LOGGER.severe(()->"properties could not be loaded (" + ex.getMessage()+")");
            return false;
        }
    }

}
