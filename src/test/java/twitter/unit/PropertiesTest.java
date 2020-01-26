package twitter.unit;

import com.socialmediaraiser.core.twitter.FollowProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PropertiesTest {
    static String userName = "RedouaneBali";

    @BeforeAll
    public static void init(){
        FollowProperties.load(userName);
    }

    @Test
    public void testIntProperty(){
        assertTrue(FollowProperties.getTargetProperties().getMinNbFollowers()>0);
    }

    @Test
    public void testFloatProperty(){
        assertTrue(FollowProperties.getTargetProperties().getMinRatio()>0);
    }

    @Test
    public void testArrayProperty(){
        assertTrue(FollowProperties.getTargetProperties().getDescription().length()>0);
    }

}
