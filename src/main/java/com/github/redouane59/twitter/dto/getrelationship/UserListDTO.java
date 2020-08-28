package com.github.redouane59.twitter.dto.getrelationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.redouane59.twitter.dto.user.UserDTOv1;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListDTO {

  private List<UserDTOv1> users;
  @JsonProperty("next_cursor_str")
  private String          nextCursor;
  @JsonProperty("previous_cursor_str")
  private String          previousCursorStr;
}
