package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.list.TwitterList.TwitterListData;
import io.github.redouane59.twitter.dto.list.TwitterListList;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class TwitterListListDeserializerTest {

  private File            twitterListListFileV2 = new File(
      getClass()
          .getClassLoader()
          .getResource("tests/twitter_list_list_example_v2.json")
          .getFile()
  );
  private TwitterListList twitterListList       =
      TwitterClient.OBJECT_MAPPER.readValue(twitterListListFileV2, TwitterListList.class);

  public TwitterListListDeserializerTest() throws IOException {
  }

  @Test
  public void testDataV2() {
    assertNotNull(twitterListList);
    TwitterListData twitterListData = twitterListList.getData().get(0);
    assertEquals("1451305624956858369", twitterListData.getId());
    assertNotNull(twitterListList);
    assertEquals("Test List",
                 twitterListData.getName());
    assertEquals("2244994945", twitterListData.getOwnerId());
    assertNotNull(twitterListList.getIncludes());
    assertNotNull(twitterListList.getIncludes().getUsers());
    assertEquals("2244994945", twitterListList.getIncludes().getUsers().get(0).getId());
    assertEquals("TwitterDev", twitterListList.getIncludes().getUsers().get(0).getName());
    assertEquals("Twitter Dev", twitterListList.getIncludes().getUsers().get(0).getDisplayedName());
    assertNotNull(twitterListList.getIncludes().getUsers().get(0).getCreatedAt());
  }
}
