package io.github.redouane59.twitter.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonHelper {

  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .findAndRegisterModules();

  public static String toJson(Object value) throws JsonProcessingException {
    return OBJECT_MAPPER.writeValueAsString(value);
  }

  public static <T> T fromJson(String value, Class<T> clazz) throws JsonProcessingException {
    return OBJECT_MAPPER.readValue(value, clazz);
  }

  public static <T> T fromJson(TreeNode node, Class<T> clazz) throws JsonProcessingException {
    return OBJECT_MAPPER.treeToValue(node, clazz);
  }

  public static <T> T fromJson(TreeNode node, JavaType javaType) throws JsonProcessingException {
    return OBJECT_MAPPER.treeToValue(node, javaType);
  }

  public static <T> T fromJson(String value, JavaType javaType) throws JsonProcessingException {
    return OBJECT_MAPPER.readValue(value, javaType);
  }

  /**
   * Check if the string supplied is valid json
   */
  public static boolean isValidJSON(String json) {
    try {
      OBJECT_MAPPER.readTree(json);
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
