package io.github.redouane59.twitter.dto.list;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TwitterListMember {

  private TwitterListMemberData data;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  @JsonInclude(Include.NON_DEFAULT)
  public static class TwitterListMemberData {

    @JsonProperty("user_id")
    String userId;
  }
}
