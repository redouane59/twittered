package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.dm.DmListAnswer;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class DMDeserializarTest {

  private File         dmListFile = new File(getClass().getClassLoader().getResource("tests/dm_list_example.json").getFile());
  private DmListAnswer dmList     = TwitterClient.OBJECT_MAPPER.readValue(dmListFile, DmListAnswer.class);

  public DMDeserializarTest() throws IOException {
  }


  @Test
  public void testEvenCount() {
    assertEquals(2, dmList.getEvents().size());
  }

  @Test
  public void testNextCursor() {
    assertEquals("MTM5Nzc5NTQ2NjIzMDgyOTA1OQ", dmList.getNextCursor());
  }

  @Test
  public void testGetDmId() {
    assertEquals("1399066074671370249", dmList.getEvents().get(0).getId());
  }

  @Test
  public void testGetSenderId() {
    assertEquals("1120050519182016513", dmList.getEvents().get(0).getMessageCreate().getSenderId());
  }

  @Test
  public void testGetText() {
    assertEquals("Bonjour", dmList.getEvents().get(0).getText());
  }

}



