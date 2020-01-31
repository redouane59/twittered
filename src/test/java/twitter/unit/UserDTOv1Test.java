package twitter.unit;

import com.socialmediaraiser.core.twitter.helpers.dto.user.UserDTOv1;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserDTOv1Test {

    @Test
    public void testHashCode() {
        UserDTOv1 user = UserDTOv1.builder().id("12345").build();
        UserDTOv1 user2 = UserDTOv1.builder().id("23456").build();
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    // @todo test diff dates string
}
