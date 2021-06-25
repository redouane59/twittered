package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.dm.DirectMessage;
import io.github.redouane59.twitter.dto.dm.DmEvent;
import io.github.redouane59.twitter.dto.dm.DmListAnswer;
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
    assertEquals(2, dmList.getDirectMessages().size());
  }

  @Test
  public void testNextCursor() {
    assertEquals("MTM5Nzc5NTQ2NjIzMDgyOTA1OQ", dmList.getNextCursor());
  }

  @Test
  public void testGetDmId() {
    assertEquals("1399066074671370249", dmList.getDirectMessages().get(0).getId());
  }

  @Test
  public void testGetSenderId() {
    assertEquals("1120050519182016513", dmList.getDirectMessages().get(0).getMessageCreate().getSenderId());
  }

  @Test
  public void testGetText() {
    assertEquals("Bonjour", dmList.getDirectMessages().get(0).getText());
  }

  @Test
  public void testGetTimeStamp() {
    assertEquals("1622398320249", dmList.getDirectMessages().get(0).getCreatedTimeStamp());
  }

  @Test
  public void testSerializeDM() throws JsonProcessingException {
    String text   = "Hello World!";
    String userId = "12345";
    String
        expectingString =
        "{\"event\":{\"type\":\"message_create\",\"message_create\":{\"target\":{\"recipient_id\":\"12345\"},\"message_data\":{\"text\":\"Hello World!\"}}}}";
    assertEquals(expectingString, TwitterClient.OBJECT_MAPPER.writeValueAsString(DmEvent.builder().event(new DirectMessage(text, userId)).build()));
  }
}
