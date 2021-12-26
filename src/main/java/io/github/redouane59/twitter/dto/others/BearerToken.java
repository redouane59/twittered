package io.github.redouane59.twitter.dto.others;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.redouane59.twitter.signature.Scope;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BearerToken {

  @JsonProperty("token_type")
  private String      tokenType;
  @JsonProperty("access_token")
  private String      accessToken;
  @JsonProperty("expires_in")
  private int         expiresIn;
  @JsonProperty("refresh_token")
  private String      refreshToken;
  @JsonDeserialize(using = ScopeDeserializer.class)
  private List<Scope> scope;

  public static class ScopeDeserializer extends StdDeserializer<List<Scope>> {

    protected ScopeDeserializer() {
      super(ScopeDeserializer.class);
    }

    @Override
    public List<Scope> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
    throws IOException {
      List<String> scopeStringList = new ArrayList<>(Arrays.asList(jsonParser.getText().split(" ")));
      return scopeStringList.stream().map(Scope::getValue).collect(Collectors.toList());
    }
  }
}
