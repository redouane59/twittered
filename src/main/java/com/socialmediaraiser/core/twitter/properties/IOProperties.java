package com.socialmediaraiser.core.twitter.properties;
import lombok.Data;

@Data
public class IOProperties {
    private String resultColumn;
    private String tabName;
    private int followDateIndex;
    private String id;
    private boolean useRFA;
    private String rfaRange;
}
