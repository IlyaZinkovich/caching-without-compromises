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
  private final String cacheName;

  RedisCacheable(
      RedisAsyncCommands<String, String> redis,
      Serializer<T> jsonSerializable, final String cacheName) {
    this.redis = redis;
    this.jsonSerializable = jsonSerializable;
    this.cacheName = cacheName;
  }

  @Override
  public CompletableFuture<T> getCachedOrLoad(
      String key, Supplier<CompletableFuture<T>> loader) {
    return getCached(key).thenCompose(cachedValue ->
        deserialize(cachedValue).orElseGet(() -> loadAndCache(key, loader)));
  }

  private CompletableFuture<Optional<String>> getCached(
      String key) {
    return redis.get(toRedisKey(key))
        .toCompletableFuture()
        .thenApply(Optional::ofNullable)
        .exceptionally(error -> {
          log.warn("Unable to get cached value for key {} in cache {}.",
              key, cacheName, error);
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

  private CompletionStage<T> cache(String key, T value) {
    return redis.set(toRedisKey(key), jsonSerializable.toJson(value))
        .thenApply(result -> value)
        .exceptionally(error -> {
          log.warn("Unable to cache value {} for key {} in cache {}.",
              value, key, cacheName, error);
          return value;
        });
  }

  private String toRedisKey(final String key) {
    return String.format("%s:%s", cacheName, key);
  }
}
