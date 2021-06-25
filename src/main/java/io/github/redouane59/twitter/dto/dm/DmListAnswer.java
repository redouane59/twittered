package io.github.redouane59.twitter.dto.dm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DmListAnswer {

  @JsonProperty("next_cursor")
  private String              nextCursor;
  @JsonProperty("events")
  private List<DirectMessage> directMessages;
  private JsonNode            apps;


}
