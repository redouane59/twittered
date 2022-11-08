package io.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.dto.user.UserV2.UserData;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserList {

  @Builder.Default
  private List<UserData> data = new ArrayList<>();
  private UserMeta       meta;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserMeta {

    @JsonProperty("result_count")
    private int    resultCount;
    @JsonProperty("next_token")
    private String nextToken;
    @JsonProperty("previous_token")
    private String previousToken;
  }
}
