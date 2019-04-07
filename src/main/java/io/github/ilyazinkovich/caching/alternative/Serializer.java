package io.github.ilyazinkovich.caching.alternative;

import java.util.Optional;

interface Serializer<T> {

  String toJson(T value);

  Optional<T> fromJson(String json);
}
