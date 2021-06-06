package com.github.redouane59.twitter.dto.dm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.redouane59.twitter.dto.dm.DirectMessage.MessageCreate.MessageData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DirectMessage {

  private String        id;
  @JsonProperty("created_timestamp")
  private String        createdTimeStamp;
  private String        type = "message_create";
  @JsonProperty("message_create")
  private MessageCreate messageCreate;

  public DirectMessage(String text, String userId) {
    this.messageCreate = new MessageCreate();
    this.messageCreate.setMessageData(new MessageData(text, null));
    this.messageCreate.setTarget(new Target(userId));
  }

  @JsonIgnore
  public String getText() {
    if (this.getMessageCreate() == null || this.getMessageCreate().getMessageData() == null) {
      return null;
    }
    return this.getMessageCreate().getMessageData().getText();
  }

  @lombok.Setter
  @lombok.Getter
  public static class MessageCreate {

    @JsonProperty("sender_id")
    private String      senderId;
    @JsonProperty("source_app_id")
    private String      sourceAppId;
    @JsonProperty("message_data")
    private MessageData messageData;
    private Target      target;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class MessageData {

      private String   text;
      private JsonNode entities;
    }
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  public static class Target {

    @JsonProperty("recipient_id")
    private String recipientId;
  }

}