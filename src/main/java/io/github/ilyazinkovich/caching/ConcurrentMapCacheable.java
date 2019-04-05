package io.github.ilyazinkovich.caching;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class ConcurrentMapCacheable<T> implements Cacheable<T> {

  private final ConcurrentMap<String, T> cache;

  public ConcurrentMapCacheable(ConcurrentMap<String, T> cache) {
    this.cache = cache;
  }

  @Override
  public CompletionStage<T> getCachedOrLoad(final String key,
      final Supplier<CompletionStage<T>> loader) {
    return Optional.ofNullable(cache.get(key))
        .map(CompletableFuture::completedFuture)
        .orElseGet(() -> {
          CompletionStage<T> load = loader.get();
          load.thenAccept(value -> cache.put(key, value));
          return load.toCompletableFuture();
        });
  }
}
