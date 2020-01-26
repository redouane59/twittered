package com.socialmediaraiser.core.twitter.helpers;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.socialmediaraiser.core.twitter.FollowProperties;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;


public class GoogleAuthorizeUtil {

    private GoogleAuthorizeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static GoogleCredential authorize() throws IOException {

        String jsonInString = JsonHelper.OBJECT_MAPPER.writeValueAsString(FollowProperties.getGoogleCredentials());

        InputStream inputStream = new ByteArrayInputStream(jsonInString.getBytes());

        return GoogleCredential
                .fromStream(inputStream)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    }
}
