package io.github.redouane59.twitter.dto.dm;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
public class DmParameters {

  @JsonProperty("conversation_type")
  @Builder.Default
  private String       conversationType = "Group";
  @JsonProperty("message")
  private DmMessage    message;
  @JsonProperty("participant_ids")
  private List<String> participantIds;

  public DmParameters(List<String> participantIds, String text) {
    this.participantIds   = participantIds;
    this.message          = DmMessage.builder().text(text).build();
    this.conversationType = "Group";
  }

  @Builder
  @Getter
  public static class DmMessage {

    private String text;
    private String attachments; // @to be improved
  }
}
