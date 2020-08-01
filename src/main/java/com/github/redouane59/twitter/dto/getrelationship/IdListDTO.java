package com.github.redouane59.twitter.dto.getrelationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IdListDTO {
    private List<String> ids;
    @JsonProperty("next_cursor_str")
    private String nextCursor;
    @JsonProperty("previous_cursor_str")
    private String previousCursorStr;
}
