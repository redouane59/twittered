package io.github.redouane59.twitter.helpers;

import com.github.scribejava.core.httpclient.multipart.FileByteArrayBodyPartPayload;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestHelper extends AbstractRequestHelper {

  public RequestHelper(TwitterCredentials twitterCredentials) {
    super(twitterCredentials);
  }

  public RequestHelper(TwitterCredentials twitterCredentials, OAuth10aService service) {
    super(twitterCredentials, service);
  }

  public <T> Optional<T> postRequestWithBodyJson(String url, Map<String, String> parameters, String requestBodyJson, Class<T> classType) {
    return makeRequest(Verb.POST, url, parameters, requestBodyJson, true, classType);
  }

  public <T> Optional<T> postRequest(String url, Map<String, String> parameters, Class<T> classType) {
    return postRequestWithBodyJson(url, parameters, null, classType);
  }

  public <T> Optional<T> postRequestWithoutSign(String url, Map<String, String> parameters, Class<T> classType) {
    return makeRequest(Verb.POST, url, parameters, null, false, classType);
  }

  public <T> Optional<T> uploadMedia(String url, File file, Class<T> classType) {
    try {
      return uploadMedia(url, file.getName(), Files.readAllBytes(file.toPath()), classType);
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
      return Optional.empty();
    }
  }

  public <T> Optional<T> uploadMedia(String url, String fileName, byte[] data, Class<T> classType) {
    OAuthRequest request = new OAuthRequest(Verb.POST, url);
    request.initMultipartPayload();
    request.addBodyPartPayloadInMultipartPayload(new FileByteArrayBodyPartPayload("application/octet-stream", data, "media", fileName));
    return makeRequest(request, true, classType);
  }

  public <T> Optional<T> putRequest(String url, String body, Class<T> classType) {
    return makeRequest(Verb.PUT, url, null, body, true, classType);
  }

  @Override
  public <T> Optional<T> getRequest(String url, Class<T> classType) {
    return getRequestWithParameters(url, null, classType);
  }


  @Override
  public <T> Optional<T> getRequestWithParameters(String url, Map<String, String> parameters, Class<T> classType) {
    return makeRequest(Verb.GET, url, parameters, null, true, classType);
  }

  @Override
  protected void signRequest(OAuthRequest request) {
    getService().signRequest(getTwitterCredentials().asAccessToken(), request);
  }

}
