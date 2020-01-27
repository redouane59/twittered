package twitter.unit;

import com.socialmediaraiser.core.twitter.User;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserTest {

    @Test
    public void testHashCode() {
        User user = User.builder().id("12345").build();
        User user2 = User.builder().id("23456").build();
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    public void testUserDiffDate0() {
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(new Date())
                .build();
        assertEquals(0, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    public void testUserDiffDate7() {
        final Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.add(Calendar.DATE, -7);
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(lastUpdate.getTime())
                .build();
        assertEquals(7, user.getDaysBetweenFollowAndLastUpdate());
    }
}
