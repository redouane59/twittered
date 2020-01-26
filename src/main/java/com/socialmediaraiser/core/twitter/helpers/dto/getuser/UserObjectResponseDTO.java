package com.socialmediaraiser.core.twitter.helpers.dto.getuser;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class UserObjectResponseDTO {
    private List<UserDTO> data;
    private IncludesDTO includes;
    private JsonNode errors;
}
