package io.github.redouane59.twitter.dto.dm;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {
  @JsonProperty("MessageCreate")
  MESSAGE_CREATE,
  @JsonProperty("ParticipantsJoin")
  PARTICIPANTS_JOIN,
  @JsonProperty("ParticipantsLeave")
  PARTICIPANTS_LEAVE;


}
