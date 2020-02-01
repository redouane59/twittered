package twitter.unit;

import com.socialmediaraiser.twitter.IUser;

import com.socialmediaraiser.twitter.helpers.JsonHelper;
import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDeserializerTestV1 {

    private  File userFile1 = new File(getClass().getClassLoader().getResource("tests/user_example_v1.json").getFile());
    private IUser userV1 = JsonHelper.OBJECT_MAPPER.readValue(userFile1, UserDTOv1.class);

    public UserDeserializerTestV1() throws IOException {
    }


    @Test
    public void testGetUserIdV1() {
        assertEquals("6253282", userV1.getId());
    }

    @Test
    public void testGetUserScreenNameV1() {
        assertEquals("TwitterAPI", userV1.getName());
    }

    @Test
    public void testGetUserLocationV1() {
        assertEquals("San Francisco, CA", userV1.getLocation());
    }

    @Test
    public void testGetUserDescriptionV1() {
        assertEquals("The Real Twitter API. Tweets about API changes, service issues and our Developer Platform. Don't get an answer? It's on my website.", userV1.getDescription());
    }

    @Test
    public void testGetUserFollowersCount() {
        assertEquals(6133636, userV1.getFollowersCount());
    }

    @Test
    public void testGetUserFollowingCount() {
        assertEquals(12, userV1.getFollowingCount());
    }

    @Test
    public void testGetUserDateOfCreation() {
        assertEquals(JsonHelper.getDateFromTwitterString("Wed May 23 06:01:13 +0000 2007"), userV1.getDateOfCreation());
    }




}



