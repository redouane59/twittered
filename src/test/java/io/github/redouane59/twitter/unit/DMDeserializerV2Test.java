package io.github.redouane59.twitter.unit;

import static org.junit.Assert.assertEquals;

import io.github.redouane59.twitter.dto.dm.DirectMessageV2;
import io.github.redouane59.twitter.dto.dm.DmEventV2;
import io.github.redouane59.twitter.dto.dm.EventType;
import io.github.redouane59.twitter.helpers.JsonHelper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class DMDeserializerV2Test {

  private File            dmFile = new File(getClass().getClassLoader().getResource("tests/dm_simple_example_v2.json").getFile());
  private DirectMessageV2 dmList = JsonHelper.OBJECT_MAPPER.readValue(dmFile, DirectMessageV2.class);

  public DMDeserializerV2Test() throws IOException {
  }


  @Test
  public void testData() {
    DmEventV2 event = dmList.getData().get(0);
    assertEquals("1580705921830768647", event.getId());
    assertEquals("1580705921830768643", event.getDmConversationId());
    assertEquals(EventType.MESSAGE_CREATE, event.getEventType());
    assertEquals("17200003", event.getSenderId());
    assertEquals("2022-10-13T23:43:54.000Z", event.getCreatedAt());
  }

  @Test
  public void testMeta() {
    assertEquals(1, dmList.getMeta().getResultCount());
    assertEquals("18LAA5FFPEKJA52G0G00ZZZZ", dmList.getMeta().getNextToken());
    assertEquals("1BLC45FFPEKJA52G0S00ZZZZ", dmList.getMeta().getPreviousToken());
  }

}
