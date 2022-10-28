package io.github.redouane59.twitter.dto.dm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DmEventV2 {

  @JsonProperty("event_type")
  private EventType eventType;
  private String    id;
  private String    text;
  @JsonProperty("sender_id")
  private String    senderId;
  @JsonProperty("created_at")
  private String    createdAt;
  @JsonProperty("dm_conversation_id")
  private String    dmConversationId;

  void setDmConversationId(final String dmConversationId) {
    this.dmConversationId = dmConversationId;
  }
}
