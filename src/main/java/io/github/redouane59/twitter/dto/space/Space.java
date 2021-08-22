package io.github.redouane59.twitter.dto.space;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.dto.tweet.TweetV2.Includes;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Space {

  private SpaceData data;
  private Includes  includes;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  public static class SpaceData {

    private String       id;
    private String       state;
    @JsonProperty("created_at")
    private String       createdAt;
    @JsonProperty("host_ids")
    private List<String> hostIds;
    private String       lang;
    @JsonProperty("is_ticketed")
    private boolean      isTicketed;
    @JsonProperty("invited_user_ids")
    private List<String> invitedUserIds;
    @JsonProperty("participant_count")
    private int          participantcount;
    @JsonProperty("scheduled_start")
    private String       scheduledStart;
    @JsonProperty("speaker_ids")
    private List<String> speakerIds;
    @JsonProperty("started_at")
    private String       startedAt;
    private String       title;
    @JsonProperty("updated_at")
    private String       updatedAt;
  }

}
