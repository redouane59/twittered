package com.socialmediaraiser.twitter.dto.tweet;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContextAnnotation {

    private Domain domain;
    private Entity entity;

    @Getter
    @Setter
    public static class Domain{
        private String id;
        private String name;
        private String description;
    }

    @Getter
    @Setter
    public static class Entity{
        private String id;
        private String name;
        private String description;
    }

}
