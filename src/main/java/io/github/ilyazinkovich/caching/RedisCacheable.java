package io.github.ilyazinkovich.caching;

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
  private final JsonSerializable<T> jsonSerializable;

  public RedisCacheable(
      RedisAsyncCommands<String, String> redis,
      JsonSerializable<T> jsonSerializable) {
    this.redis = redis;
    this.jsonSerializable = jsonSerializable;
  }

  @Override
  public CompletionStage<T> getCachedOrLoad(
      String key, Supplier<CompletionStage<T>> loader) {
    return getCachedValue(key).thenCompose(cachedValue ->
        deserialize(cachedValue).orElseGet(() -> loadAndCache(key, loader)));
  }

  private CompletionStage<Optional<String>> getCachedValue(
      String key) {
    return redis.get(key).thenApply(Optional::ofNullable)
        .exceptionally(error -> {
          log.warn("Unable to get cached value for key {}.", key, error);
          return Optional.empty();
        });
  }

  private Optional<CompletionStage<T>> deserialize(
      Optional<String> cachedValue) {
    return cachedValue
        .flatMap(jsonSerializable::fromJson)
        .map(CompletableFuture::completedFuture);
  }

  private CompletionStage<T> loadAndCache(
      String key, Supplier<CompletionStage<T>> loader) {
    return loader.get().thenCompose(value ->
        redis.set(key, jsonSerializable.toJson(value))
            .thenApply(result -> value)
            .exceptionally(error -> {
              log.warn("Unable to cache value {} for key {}.", value, key, error);
              return value;
            })
    );
  }
}