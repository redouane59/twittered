package com.socialmediaraiser.twitter.dto.tweet;

public enum TweetType {
    RETWEETED("retweeted"),
    QUOTED("quoted"),
    REPLIED_TO("replied_to");

    public final String label;

    private TweetType(String label) {
        this.label = label;
    }

    public static TweetType valueOfLabel(String label) {
        for (TweetType e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
