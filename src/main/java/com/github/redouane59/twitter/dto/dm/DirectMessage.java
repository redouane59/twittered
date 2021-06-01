package com.github.redouane59.twitter.dto.dm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DirectMessage {

  private String        id;
  @JsonProperty("created_timestamp")
  private String        createdTimeStamp;
  private String        type;
  @JsonProperty("message_create")
  private MessageCreate messageCreate;
  private Target        target;

  public String getText() {
    if (this.getMessageCreate() == null || this.getMessageCreate().getMessageData() == null) {
      return null;
    }
    return this.getMessageCreate().getMessageData().getText();
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  public static class MessageCreate {

    @JsonProperty("sender_id")
    private String      senderId;
    @JsonProperty("source_app_id")
    private String      sourceAppId;
    @JsonProperty("message_data")
    private MessageData messageData;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
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