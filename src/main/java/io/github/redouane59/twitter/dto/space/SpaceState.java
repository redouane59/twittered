package io.github.redouane59.twitter.dto.space;

import lombok.Getter;

public enum SpaceState {
  LIVE("live"),
  SCHEDULED("scheduled");

  @Getter
  public final String label;

  SpaceState(String label) {
    this.label = label;
  }

}
