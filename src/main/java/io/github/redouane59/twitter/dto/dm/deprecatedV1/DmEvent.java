package io.github.redouane59.twitter.dto.dm.deprecatedV1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DmEvent {

  private DirectMessage event;

}
