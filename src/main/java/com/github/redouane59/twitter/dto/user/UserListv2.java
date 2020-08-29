package com.github.redouane59.twitter.dto.user;

import com.github.redouane59.twitter.dto.user.UserV2.UserData;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserListv2 {

  private List<UserData> data;

}
