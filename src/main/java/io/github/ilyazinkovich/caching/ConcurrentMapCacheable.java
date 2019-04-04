package io.github.ilyazinkovich.caching;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class ConcurrentMapCacheable<T> implements Cacheable<T> {

  private final ConcurrentMap<String, T> cache;

  public ConcurrentMapCacheable(ConcurrentMap<String, T> cache) {
    this.cache = cache;
  }

  @Override
  public CompletableFuture<T> getCachedOrLoad(
      String key, Supplier<CompletableFuture<T>> loader) {
    return Optional.ofNullable(cache.get(key))
        .map(CompletableFuture::completedFuture)
        .orElseGet(() -> {
          CompletableFuture<T> load = loader.get();
          load.thenAccept(value -> cache.put(key, value));
          return load;
        });
  }
}
