package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.list.TwitterList;
import io.github.redouane59.twitter.dto.list.TwitterList.TwitterListData;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class TwitterListDeserializerTest {

  private File        twitterListFileV2 = new File(
      getClass()
          .getClassLoader()
          .getResource("tests/twitter_list_example_v2.json")
          .getFile()
  );
  private TwitterList twitterList       =
      TwitterClient.OBJECT_MAPPER.readValue(twitterListFileV2, TwitterList.class);

  public TwitterListDeserializerTest() throws IOException {
  }

  @Test
  public void testDataV2() {
    assertNotNull(twitterList);
    TwitterListData twitterListData = twitterList.getData();
    assertEquals("84839422", twitterListData.getId());
    assertNotNull(twitterList);
    assertEquals("Official Twitter Accounts",
                 twitterListData.getName());
    assertEquals(906, twitterListData.getFollowerCount());
    assertEquals("783214", twitterListData.getOwnerId());
    assertNotNull(twitterList.getIncludes());
    assertNotNull(twitterList.getIncludes().getUsers());
    assertEquals("783214", twitterList.getIncludes().getUsers().get(0).getId());
    assertEquals("TWITTER", twitterList.getIncludes().getUsers().get(0).getName());
    assertEquals("Twitter", twitterList.getIncludes().getUsers().get(0).getDisplayedName());
  }
}
