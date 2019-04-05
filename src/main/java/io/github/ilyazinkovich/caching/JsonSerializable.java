package io.github.ilyazinkovich.caching;

import java.util.Optional;

public interface JsonSerializable<T> {

  String toJson(T value);

  Optional<T> fromJson(String json);
}
