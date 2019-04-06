package io.github.ilyazinkovich.caching.alternative;

import java.util.Optional;

public interface Serializer<T> {

  String toJson(T value);

  Optional<T> fromJson(String json);
}
