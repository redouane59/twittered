package io.github.redouane59.twitter.dto.space;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.dto.space.Space.SpaceData;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SpaceList {

  List<SpaceData> data;

  SpaceMeta meta;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  public static class SpaceMeta {

    @JsonProperty("result_count")
    private int resultCount;
  }

}