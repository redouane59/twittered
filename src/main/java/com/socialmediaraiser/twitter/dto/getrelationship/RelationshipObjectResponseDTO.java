package com.socialmediaraiser.twitter.dto.getrelationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RelationshipObjectResponseDTO {

    private RelationshipDTO relationship;

    @Data
    public static class RelationshipDTO {
        private SourceDTO source;
        private TargetDTO target;

        @Data
        public static class SourceDTO {
            @JsonProperty("can_dm")
            private boolean canDm;
            @JsonProperty("all_replies")
            private boolean allReplies;
            @JsonProperty("following_requested")
            private boolean followingRequested;
            @JsonProperty("marked_spam")
            private boolean markedSpam;
            @JsonProperty("notifications_enabled")
            private boolean notificationsEnabled;
            @JsonProperty("live_following")
            private boolean liveFollowing;
            @JsonProperty("followed_by")
            private boolean followedBy;
            private boolean muting;
            @JsonProperty("screen_name")
            private String screenName;
            private boolean blocking;
            @JsonProperty("blocked_by")
            private boolean blockedBy;
            @JsonProperty("want_retweets")
            private boolean wantRetweets;
            @JsonProperty("id_str")
            private String idStr;
            private boolean following;
            private Long id;
            @JsonProperty("following_received")
            private boolean followingReceived;
        }

        @Data
        public static class TargetDTO {
            @JsonProperty("followed_by")
            private boolean followedBy;
            @JsonProperty("screen_name")
            private String screenName;
            @JsonProperty("id_str")
            private String idStr;
            private boolean following;
            @JsonProperty("following_requested")
            private boolean followingRequested;
            private Long id;
            @JsonProperty("following_received")
            private boolean followingReceived;
        }
    }
}
