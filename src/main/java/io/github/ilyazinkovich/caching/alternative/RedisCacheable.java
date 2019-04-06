package io.github.ilyazinkovich.caching.alternative;

import io.lettuce.core.api.async.RedisAsyncCommands;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisCacheable<T> implements Cacheable<T> {

  private static final Logger log = LoggerFactory.getLogger(RedisCacheable.class);
  private final RedisAsyncCommands<String, String> redis;
  private final Serializer<T> jsonSerializable;

  RedisCacheable(
      RedisAsyncCommands<String, String> redis,
      Serializer<T> jsonSerializable) {
    this.redis = redis;
    this.jsonSerializable = jsonSerializable;
  }

  @Override
  public CompletableFuture<T> getCachedOrLoad(
      String key, Supplier<CompletableFuture<T>> loader) {
    return getCached(key).thenCompose(cachedValue ->
        deserialize(cachedValue).orElseGet(() -> loadAndCache(key, loader)));
  }

  private CompletableFuture<Optional<String>> getCached(
      String key) {
    return redis.get(key)
        .toCompletableFuture()
        .thenApply(Optional::ofNullable)
        .exceptionally(error -> {
          log.warn("Unable to get cached value for key {}.", key, error);
          return Optional.empty();
        });
  }

  private Optional<CompletableFuture<T>> deserialize(
      Optional<String> cachedValue) {
    return cachedValue
        .flatMap(jsonSerializable::fromJson)
        .map(CompletableFuture::completedFuture);
  }

  private CompletableFuture<T> loadAndCache(
      String key, Supplier<CompletableFuture<T>> loader) {
    return loader.get().thenCompose(value -> cache(key, value));
  }

  private CompletionStage<T> cache(final String key, final T value) {
    return redis.set(key, jsonSerializable.toJson(value))
        .thenApply(result -> value)
        .exceptionally(error -> {
          log.warn("Unable to cache value {} for key {}.", value, key, error);
          return value;
        });
  }
}
