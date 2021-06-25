package io.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.dto.user.UserV2.UserData;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserListV2 {

  private List<UserData> data;
  private UserMeta       meta;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class UserMeta {

    @JsonProperty("result_count")
    private int    resultCount;
    @JsonProperty("next_token")
    private String nextToken;
    @JsonProperty("previous_token")
    private String previousToken;
  }
}
