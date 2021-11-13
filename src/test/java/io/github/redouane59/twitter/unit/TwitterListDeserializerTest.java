package io.github.redouane59.twitter.unit;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.list.TwitterList;
import io.github.redouane59.twitter.dto.user.UserV1;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwitterListDeserializerTest {

    private File twitterListFileV1 = new File(
            getClass()
            .getClassLoader()
            .getResource("tests/twitter_list_example_v1.json")
            .getFile()
    );
    private TwitterList.TwitterListData[] twitterListV1s =
            TwitterClient.OBJECT_MAPPER.readValue(twitterListFileV1, TwitterList.TwitterListData[].class);

    public TwitterListDeserializerTest() throws IOException {
    }

    @Test
    public void testDataV1() {
        assertNotNull(twitterListV1s);
        assertEquals(2, twitterListV1s.length);
        TwitterList.TwitterListData firstTwitterList = twitterListV1s[0];
        assertNotNull(firstTwitterList);
        assertEquals("Guests attending the Twitter meetup on 1 March 2010 at the @twoffice",
                firstTwitterList.getDescription());
        assertEquals(116, firstTwitterList.getMemberCount());
        assertEquals("meetup-20100301", firstTwitterList.getName());
        assertEquals("8044403", firstTwitterList.getId());

        UserV1 owner = firstTwitterList.getOwner();

        assertEquals("Twitter API", owner.getDisplayedName());
        assertEquals("http://a0.twimg.com/profile_images/2284174872/7df3h38zabcvjylnyfe3_normal.png",
                owner.getProfileImageUrl());
        assertEquals("6253282", owner.getId());
        assertEquals(1444739, owner.getFollowersCount());
        assertEquals("twitterapi", owner.getName());
    }
}
