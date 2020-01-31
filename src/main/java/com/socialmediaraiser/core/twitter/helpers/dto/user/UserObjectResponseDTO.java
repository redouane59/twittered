package com.socialmediaraiser.core.twitter.helpers.dto.user;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class UserObjectResponseDTO {
    private List<UserDTOv2> data;
    private IncludesDTO includes;
    private JsonNode errors;
}
