package com.github.redouane59.twitter.dto.tweet;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TweetSearchV1DTO {

  private List<TweetDTOv1> results;
  private String           next;
}
