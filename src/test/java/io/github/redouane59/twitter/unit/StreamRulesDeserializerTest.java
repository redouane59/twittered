package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.stream.StreamRules;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class StreamRulesDeserializerTest {

  private File        rulesFile = new File(getClass().getClassLoader().getResource("tests/stream_rules_example.json").getFile());
  private StreamRules rules     = TwitterClient.OBJECT_MAPPER.readValue(rulesFile, StreamRules.class);

  public StreamRulesDeserializerTest() throws IOException {
  }

  @Test
  public void testSize() {
    assertEquals(2, rules.getData().size());
  }

  @Test
  public void testIds() {
    assertEquals("1298705840506118146", rules.getData().get(0).getId());
    assertEquals("1298706201161728002", rules.getData().get(1).getId());
  }

  @Test
  public void testValues() {
    assertEquals("algerie", rules.getData().get(0).getValue());
    assertEquals("super", rules.getData().get(1).getValue());
  }

  @Test
  public void testTags() {
    assertEquals("1", rules.getData().get(0).getTag());
    assertEquals("2", rules.getData().get(1).getTag());
  }

  @Test
  public void testMeta() {
    assertEquals("2020-08-26T20:12:54.519Z", rules.getMeta().getSent());
  }
}
