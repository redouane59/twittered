package com.socialmediaraiser.twitter.unit;

import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserDTOv1Test {

    @Test
    public void testHashCode() {
        UserDTOv1 user = UserDTOv1.builder().id("12345").build();
        UserDTOv1 user2 = UserDTOv1.builder().id("23456").build();
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    public void testEquals(){
        UserDTOv1 user = UserDTOv1.builder().id("12345").build();
        UserDTOv1 user2 = UserDTOv1.builder().id("12345").build();
        assertEquals(user, user2);
        user = UserDTOv1.builder().id("12345").build();
        user2 = UserDTOv1.builder().id("23456").build();
        assertNotEquals(user, user2);
    }
}
