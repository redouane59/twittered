package twitter.unit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.core.twitter.FollowProperties;
import com.socialmediaraiser.core.twitter.Tweet;
import com.socialmediaraiser.core.twitter.TwitterHelper;
import com.socialmediaraiser.core.twitter.helpers.JsonHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.IUser;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.TweetDataDTO;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.UserObjectResponseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonHelperTest {

    private static String ownerName = "RedTheOne";
    private JsonHelper jsonHelper = new JsonHelper();

    @BeforeAll
    public static void init(){
        FollowProperties.load(ownerName);
    }

    @Test
    public void testCastTweetFromString() throws IOException {
        String tweet = "{\"created_at\":\"Sun May 26 16:37:48 +0000 2019\",\"id\":1132687286708178945,\"id_str\":\"1132687286708178945\",\"text\":\"Right @stewartdonald3 , so can you let JR know that we've been here before but our expectations now are 100+ points\\u2026 https:\\/\\/t.co\\/pzuPKcttBk\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/android\\\" rel=\\\"nofollow\\\"\\u003eTwitter for Android\\u003c\\/a\\u003e\",\"truncated\":true,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":2897854199,\"id_str\":\"2897854199\",\"name\":\"Tracy\",\"screen_name\":\"tracysweettweet\",\"location\":\"The Edge of Reality\",\"url\":null,\"description\":\"Mam to 2 kids and 1 labrador, pretty happy with life\",\"translator_type\":\"none\",\"protected\":false,\"verified\":false,\"followers_count\":29,\"friends_count\":283,\"listed_count\":0,\"favourites_count\":1589,\"statuses_count\":347,\"created_at\":\"Sat Nov 29 18:42:44 +0000 2014\",\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/771135820199854083\\/BPszH9Z9_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/771135820199854083\\/BPszH9Z9_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/2897854199\\/1462485811\",\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"extended_tweet\":{\"full_text\":\"Right @stewartdonald3 , so can you let JR know that we've been here before but our expectations now are 100+ points next season and some entertaining bloody football! #STID\",\"display_text_range\":[0,172],\"entities\":{\"hashtags\":[{\"text\":\"STID\",\"indices\":[167,172]}],\"urls\":[],\"user_mentions\":[{\"screen_name\":\"stewartdonald3\",\"name\":\"stewart donald\",\"id\":577978827,\"id_str\":\"577978827\",\"indices\":[6,21]}],\"symbols\":[]}},\"quote_count\":0,\"reply_count\":3,\"retweet_count\":1,\"favorite_count\":2,\"entities\":{\"hashtags\":[],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/pzuPKcttBk\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/1132687286708178945\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/1\\u2026\",\"indices\":[117,140]}],\"user_mentions\":[{\"screen_name\":\"stewartdonald3\",\"name\":\"stewart donald\",\"id\":577978827,\"id_str\":\"577978827\",\"indices\":[6,21]}],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"low\",\"lang\":\"en\",\"timestamp_ms\":\"1558888668120\"}\n";
        ObjectMapper objectMapper = JsonHelper.OBJECT_MAPPER;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Tweet result = objectMapper.readValue(tweet, Tweet.class);
        assertNotNull(result);
        assertNotNull(result.getLang());
        assertNotNull(result.getId());
        assertNotNull(result.getUser());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getFavoriteCount());
        assertNotNull(result.getRetweetCount());
        assertNotNull(result.getReplyCount());
        assertNotNull(result.getText());
    }

    @Test
    public void testParseGetUserResponse() throws IOException {
        String stringResponse = "{\n" +
                "   \"data\":[\n" +
                "      {\n" +
                "         \"id\":\"92073489\",\n" +
                "         \"created_at\":\"2009-11-23T17:53:15.000Z\",\n" +
                "         \"name\":\"Red'1\",\n" +
                "         \"username\":\"RedTheOne\",\n" +
                "         \"protected\":false,\n" +
                "         \"location\":\"Madrid, Espagne\",\n" +
                "         \"url\":\"\",\n" +
                "         \"description\":\"En vérité, j'suis à ma place quand je les dérange. Jamais dans la tendance, toujours dans la bonne direction... #Lille #Montréal #Madrid @Decathlon\uD83D\uDC8D\uD83C\uDDE9\uD83C\uDDFF\uD83C\uDDEE\uD83C\uDDF9\",\n" +
                "         \"verified\":false,\n" +
                "         \"entities\":{\n" +
                "            \"description\":{\n" +
                "               \"hashtags\":[\n" +
                "                  {\n" +
                "                     \"start\":112,\n" +
                "                     \"end\":118,\n" +
                "                     \"tag\":\"Lille\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"start\":119,\n" +
                "                     \"end\":128,\n" +
                "                     \"tag\":\"Montréal\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"start\":129,\n" +
                "                     \"end\":136,\n" +
                "                     \"tag\":\"Madrid\"\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"mentions\":[\n" +
                "                  {\n" +
                "                     \"start\":137,\n" +
                "                     \"end\":147,\n" +
                "                     \"username\":\"Decathlon\"\n" +
                "                  }\n" +
                "               ]\n" +
                "            }\n" +
                "         },\n" +
                "         \"profile_image_url\":\"https://pbs.twimg.com/profile_images/1162352850661445633/c1Rw5BI0_normal.jpg\",\n" +
                "         \"stats\":{\n" +
                "            \"followers_count\":6279,\n" +
                "            \"following_count\":4153,\n" +
                "            \"tweet_count\":35399,\n" +
                "            \"listed_count\":73\n" +
                "         },\n" +
                "         \"most_recent_tweet_id\":\"1162756711595282432\",\n" +
                "         \"pinned_tweet_id\":\"1035192987008020480\",\n" +
                "         \"format\":\"detailed\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"includes\":{\n" +
                "      \"tweets\":[\n" +
                "         {\n" +
                "            \"id\":\"1162756711595282432\",\n" +
                "            \"created_at\":\"2019-08-17T16:02:57.000Z\",\n" +
                "            \"text\":\"@fbelmadi dédicace spéciale pour toi\",\n" +
                "            \"author_id\":\"92073489\",\n" +
                "            \"in_reply_to_user_id\":\"92073489\",\n" +
                "            \"referenced_tweets\":[\n" +
                "               {\n" +
                "                  \"type\":\"replied_to\",\n" +
                "                  \"id\":\"1162756655945306113\"\n" +
                "               }\n" +
                "            ],\n" +
                "            \"entities\":{\n" +
                "               \"mentions\":[\n" +
                "                  {\n" +
                "                     \"start\":0,\n" +
                "                     \"end\":9,\n" +
                "                     \"username\":\"fbelmadi\"\n" +
                "                  }\n" +
                "               ]\n" +
                "            },\n" +
                "            \"stats\":{\n" +
                "               \"retweet_count\":0,\n" +
                "               \"reply_count\":1,\n" +
                "               \"like_count\":1,\n" +
                "               \"quote_count\":0\n" +
                "            },\n" +
                "            \"possibly_sensitive\":false,\n" +
                "            \"lang\":\"fr\",\n" +
                "            \"source\":\"<a href=\\\"https://mobile.twitter.com\\\" rel=\\\"nofollow\\\">Twitter Web App</a>\",\n" +
                "            \"format\":\"detailed\"\n" +
                "         }\n" +
                "      ]\n" +
                "   }\n" +
                "}";

        UserObjectResponseDTO objectInterpretation = JsonHelper.OBJECT_MAPPER.readValue(stringResponse, UserObjectResponseDTO.class);
        assertNotNull(objectInterpretation);
        assertNotNull(objectInterpretation.getData());
        assertNotNull(objectInterpretation.getIncludes());
        assertEquals(6279, objectInterpretation.getData().get(0).getStats().getFollowersCount());
        assertTrue("92073489".equals(objectInterpretation.getIncludes().getTweets().get(0).getAuthorId()));
        IUser user = jsonHelper.jsonResponseToUserV2(stringResponse);
        assertTrue("92073489".equals(user.getId()));
        assertTrue("RedTheOne".equals(user.getUsername()));
        assertTrue("Madrid, Espagne".equals(user.getLocation()));
        assertEquals(6279, user.getFollowersCount());
        assertEquals(4153, user.getFollowingCount());
        assertEquals(35399,user.getTweetCount());
        assertNotNull(user.getDescription());
        assertNotNull(user.getDateOfCreation());
        assertNotNull(user.getLastUpdate());
    }

    @Test
    public void testJsonLongArrayToList() throws IOException {
        JsonNode jsonObject = JsonHelper.OBJECT_MAPPER.readTree("{\"ids\":[2942879319,2265596471,745614524489797632,948370055477321728,118668671,1119989974475186176,1055490002027798528,361472932,1541186544,969869708794134528,107755674,78402941,1274575344,1071445632789962754,269009879,361164030,1073348925699039234,190653363,7607262,70710408,801718929462886400,2998172923,972705140917637122,40071118,275081254,532178115,290012052,866571366425874432,1071653217937866752,1147385295379345408,2688139332,449245061,278700526,1085827252259033088,946371519965523968,1628028361,3427674675,4841197342,4915475789,2817000024,429617595,911579064904949762,314195892,1563676212,1055168135563755521,1056884109472083969,206760141,728126064346570752,1118930741663031297,2333849373,1061552488518742016,2816694103,32939851,1112467941008830464,281001495,1037951761,53429112,146185782,80810171,1052543660951199746,2341966730,891290922205446144,1042669061547667457,444919389,879611420501061632,1035589043919249414,702459408190676992,2902138942,1105167006993653760,4637254456,1029278761630552064,1064751277547184133,2178057260,915939666,818527270012911616,94798957,943584971712802818,490713259,989413462672527360,844801521648775169,920336978972987392,306854894,793394617660149760,238759529,799340297679470592,1431270728,360565432,856402623762767872,1065882212573298690,2317368368,251624397,1076222484930838528,1065562650560659458,1294296553,957680182210564096,800152432068001792,1377052002,709391526435098624,2502776569,4438798815,813371175489388544,2869236250,361812003,1014296109878009857,113398253,14755217,333444242,813388105621405696,1060197164142526464,4173821709,410244336,991304382221348865,4313509996,39083212,934384671424679937,852524199570747392,701305980,32386787,948894896357814272,524531750,1911204907,250038103,608925053,802658571418763264,900147812,1445613554,1035851965153062912,884093377008062465,1619021240,1015338559321133058,357083932,1010943272787865600,91302737,573705623,602937197,501058686,3310832543,3708010696,956886723077791745,855853304940814336,700656935498940418,920987081815490560,336701435,900293864,498662158,702457241291661312,295692501,220462772,977870482027565057,92073489,735801580969185280,3220792635,2775722571,220387296,985582717407584256,14183198,967426759078699008,716271544,2205819166,866467009223176192,362555315,579024725,972619254221672448,934449463623593986,180295980,976520974874144768,3088703339,972835401437188096,431521358,253673113,2279219111,1011598394,180719600,357762433,4871249769,3378725091,904483873,746905807757918208,866376431496036353,936880228458225664,195459265,71475684,910224584946876416,2463360517,4059946649,43725304,4626034097,830543389624061952,1057662630,126267113,52505989,78619534,88582825,4643995353,4599329901,759437737409781760,740119445071405056,866977587930771456,2333742500,2825200847,427450023,216019680,154210430,637392588,889979655574495234,2592735271,3101414667,385582764,1543351291,910189037968855042,175034180,555285801,629350882,2474074004,585704267,900715663261667329,3314804194,903202963455451137,880848090760085504,448440386,973969800,731587252019695617,556109413,897068188848066560,805413865123172353,302892667,134196350,564396129,394138775,224140014,319078287,462970454,92797314,257057825,2291083686,3181111780,2233079401,22090282,538138859,973970100,1018120640,751458148569997312,511056603,763267436409942016,498348860,2373827346,492648852,4656625635,844028959,4616015975,1018336134,888469164918222849,773199348649844736,148688823,4857955701,24744541,793961648,1308821604,3390937060,1245515689,778032558932131840,863885255912108032,54951973,737265028638908416,96322637,523423376,398002001,15425377,84204116,1280529294,570775694,329073521,50492265,1249587637,331526306,1327821240,4521548307,101968742,757556189039296512,3304990276,1120476301,585271160,1038475093,79145543,112451555,477893484,560375737,4550587463,765849247262633984,2276493893,323759644,3093217174,829335740437897216,757262283647188992,380971294,929277739,46859172,7627832,1976143068],\"next_cursor\":0,\"next_cursor_str\":\"0\",\"previous_cursor\":0,\"previous_cursor_str\":\"0\",\"total_count\":null}");
        List<String> result = this.jsonHelper.jsonLongArrayToList(jsonObject);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testParseTwitterFile() throws IOException, ParseException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tweet.json").getFile());
        assertNotNull(file);
        TweetDataDTO[] tweets = JsonHelper.OBJECT_MAPPER.readValue(file, TweetDataDTO[].class);
        assertNotNull(tweets);

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

        TwitterHelper helper = new TwitterHelper("RedTheOne");

        Map<String, Integer> result = new HashMap<>();
        Date tweetDate;
        Date limitDate = sdformat.parse("2020-01-01");
        for(TweetDataDTO tweetDataDTO : tweets){
            String userId = tweetDataDTO.getTweet().getInReplyToUserId();
            if(userId!=null){
                tweetDate = JsonHelper.getDateFromTwitterString(tweetDataDTO.getTweet().getCreatedAt());
                if(tweetDate!=null && tweetDate.compareTo(limitDate)>0) {
                        result.put(userId, 1+result.getOrDefault(userId, 0));
                      //  System.out.println(tweetDate + " -> " + userId);
                }
            }
        }
        assertNotNull(result);
    }
}
