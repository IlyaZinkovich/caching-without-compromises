package io.github.ilyazinkovich.caching.alternative;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GsonSerializer<T> implements Serializer<T> {

  private static final Logger log = LoggerFactory.getLogger(GsonSerializer.class);
  private final Gson gson;
  private final Class<T> targetClass;

  GsonSerializer(final Gson gson, final Class<T> targetClass) {
    this.gson = gson;
    this.targetClass = targetClass;
  }

  @Override
  public String toJson(final T value) {
    return gson.toJson(value);
  }

  @Override
  public Optional<T> fromJson(final String json) {
    Optional<T> result;
    try {
      result = Optional.ofNullable(gson.fromJson(json, targetClass));
    } catch (JsonParseException exception) {
      log.warn("Failed to parse json {} to class {}.", json, targetClass);
      result = Optional.empty();
    }
    return result;
  }
}
