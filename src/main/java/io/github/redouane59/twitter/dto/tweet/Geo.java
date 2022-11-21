package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class Geo {

  @JsonProperty("place_id")
  private String      placeId;
  private Coordinates coordinates;

  @Getter
  @Setter
  @Jacksonized
  public static class Coordinates {

    private String   type;
    private double[] coordinates;

  }

}
