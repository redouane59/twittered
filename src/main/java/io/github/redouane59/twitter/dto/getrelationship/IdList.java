package io.github.redouane59.twitter.dto.getrelationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdList {

  private List<String> ids;
  @JsonProperty("next_cursor_str")
  private String       nextCursor;
  @JsonProperty("previous_cursor_str")
  private String       previousCursorStr;
}
