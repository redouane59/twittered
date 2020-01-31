package twitter.unit;

import com.socialmediaraiser.core.twitter.IUser;
import com.socialmediaraiser.core.twitter.helpers.JsonHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.user.UserDTOv2;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDeserializerTestV2 {

    private  File userFile2 = new File(getClass().getClassLoader().getResource("tests/user_example_v2.json").getFile());
    private IUser userV2 = JsonHelper.OBJECT_MAPPER.readValue(userFile2, UserDTOv2.class);

    public UserDeserializerTestV2() throws IOException {
    }


    @Test
    public void testGetUserIdV2() {
        assertEquals("2244994945", userV2.getId());
    }

    @Test
    public void testGetUserScreenNameV2() {
        assertEquals("TwitterDev", userV2.getName());
    }

    @Test
    public void testGetUserLocationV2() {
        assertEquals("Internet", userV2.getLocation());
    }

    @Test
    public void testGetUserDescriptionV2() {
        assertEquals("Your official source for Twitter Platform news, updates & events. Need technical help? Visit https://t.co/mGHnxZU8c1 ⌨️ #TapIntoTwitter", userV2.getDescription());
    }

    @Test
    public void testGetUserFollowersCount() {
        assertEquals(503251, userV2.getFollowersCount());
    }

    @Test
    public void testGetUserFollowingCount() {
        assertEquals(1474, userV2.getFollowingCount());
    }

    @Test
    public void testGetUserTweetsCount() {
        assertEquals(3419, userV2.getTweetCount());
    }


    @Test
    public void testGetUserDateOfCreation() {
        assertEquals(JsonHelper.getDateFromTwitterDateV2("2013-12-14T04:35:55.000Z"), userV2.getDateOfCreation());
    }




}



