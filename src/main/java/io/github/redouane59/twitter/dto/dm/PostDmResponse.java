package io.github.redouane59.twitter.dto.dm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PostDmResponse {

  private PostDmEvent data;

  @Getter
  public static class PostDmEvent {

    @JsonProperty("dm_conversation_id")
    private String dmConversationId;
    @JsonProperty("dm_event_id")
    private String dmEventId;

  }
}
