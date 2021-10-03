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
public class TwitterList {

  private TwitterListData data;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  @JsonInclude(Include.NON_DEFAULT)
  public static class TwitterListData {

    String id;
    String name;
    String description;
    @JsonProperty("private")
    boolean isPrivate;
    boolean deleted;
  }
}
