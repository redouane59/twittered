package io.github.redouane59.twitter.dto.tweet.entities;

import java.util.List;

public interface Entities {

  List<? extends HashtagEntity> getHashtags();

  List<? extends UrlEntity> getUrls();

  List<? extends SymbolEntity> getSymbols();

  List<? extends UserMentionEntity> getUserMentions();
}
