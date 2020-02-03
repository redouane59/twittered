package com.socialmediaraiser.twitter.dto.getrelationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class IdListDTO {
    private List<String> ids;
    @JsonProperty("next_cursor_str")
    private String nextCursor;
    @JsonProperty("previous_cursor_str")
    private String previousCursorStr;
}
