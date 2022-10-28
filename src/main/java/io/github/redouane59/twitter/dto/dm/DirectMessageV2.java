package io.github.redouane59.twitter.dto.dm;

import io.github.redouane59.twitter.dto.user.UserList.UserMeta;
import java.util.List;
import lombok.Getter;

@Getter
public class DirectMessageV2 {

  List<DmEventV2> data;

  UserMeta meta;
}
