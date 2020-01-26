package twitter.unit;

import com.socialmediaraiser.core.twitter.FollowProperties;
import com.socialmediaraiser.core.twitter.User;
import com.socialmediaraiser.core.twitter.scoring.Criterion;
import com.socialmediaraiser.core.twitter.scoring.UserScoringEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class ScoringTest {

    private static String ownerName = "RedouaneBali";

    @BeforeEach
    void init(){
        FollowProperties.load(ownerName);
        FollowProperties.getScoringProperties().getProperty(Criterion.LAST_UPDATE).setBlocking(false);
    }

    @Test
    void testScoringZero(){
        User user = new User();
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(0,scoring.getUserScore(user));
    }

    @Test
    void testScoringOneMatchDescription(){
        FollowProperties.getTargetProperties().setDescription("a,b,c");
        User user = new User();
        user.setDescription("a");
        FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).setActive(true);
        FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
        user.setDescription("b");
        assertEquals(FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
        user.setDescription("c");
        assertEquals(FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
    }

    @Test
    void testScoringSeveralMatchesDescription(){
        User user = new User();
        user.setDescription("a b c ");
        FollowProperties.getTargetProperties().setDescription("a,b,c");
        FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).setActive(true);
        FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {2} when followers: {0} followings: {1}")
    @CsvSource(value = {"1000, 1000, 20," +
                        "100, 1000, 0," +
                        "1000, 100, 0"})
    void testScoringMinMaxRatio(String nbFollowers, String nbFollowings, String exceptedResult){
        User user = new User();
        user.setFollowersCount(Integer.parseInt(nbFollowers));
        user.setFollowingCount(Integer.parseInt(nbFollowings));
        FollowProperties.getTargetProperties().setMinRatio((float)0.5);
        FollowProperties.getTargetProperties().setMaxRatio((float)1.5);
        FollowProperties.getScoringProperties().getProperty(Criterion.RATIO).setMaxPoints(20);
        FollowProperties.getScoringProperties().getProperty(Criterion.RATIO).setActive(true);
        FollowProperties.getScoringProperties().getProperty(Criterion.NB_FOLLOWERS).setActive(false);
        FollowProperties.getScoringProperties().getProperty(Criterion.NB_FOLLOWINGS).setActive(false);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals((int)Integer.parseInt(exceptedResult), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {1} when followers: {0}")
    @CsvSource(value = {"1000, 10," +
            "100, 0," +
            "10000, 0"})
    void testScoringMinMaxFollowers(String nbFollowers, String exceptedResult){
        User user = new User();
        user.setFollowersCount(Integer.parseInt(nbFollowers));
        FollowProperties.getTargetProperties().setMinNbFollowers(500);
        FollowProperties.getTargetProperties().setMaxNbFollowers(5000);
        FollowProperties.getScoringProperties().getProperty(Criterion.NB_FOLLOWERS).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals((int)Integer.parseInt(exceptedResult), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {1} when followings: {0}")
    @CsvSource(value = {"1000, 10," +
            "100, 0," +
            "10000, 0"})
    void testScoringMinMaxFollowings(String nbFollowings, String exceptedResult){
        User user = new User();
        user.setFollowingCount(Integer.parseInt(nbFollowings));
        FollowProperties.getTargetProperties().setMinNbFollowings(500);
        FollowProperties.getTargetProperties().setMaxNbFollowings(5000);
        FollowProperties.getScoringProperties().getProperty(Criterion.NB_FOLLOWINGS).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals((int)Integer.parseInt(exceptedResult), scoring.getUserScore(user));
    }

    @Test
    void testLimit(){
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertNotEquals(0, scoring.getLimit());
    }

    @Test
    void testBlockingProperty(){
        User user = new User();
        user.setDescription("x");
        user.setLang("fr");
        user.setFollowersCount(100);
        FollowProperties.getTargetProperties().setDescription("a,b,c");
        FollowProperties.getTargetProperties().setMinNbFollowers(10);
        FollowProperties.getTargetProperties().setMaxNbFollowers(1000);
        FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).setActive(true);
        FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).setMaxPoints(10);
        FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).setBlocking(true);
        FollowProperties.getScoringProperties().getProperty(Criterion.NB_FOLLOWERS).setActive(true);
        FollowProperties.getScoringProperties().getProperty(Criterion.NB_FOLLOWERS).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(0, scoring.getUserScore(user));
    }


}
