package io.github.ilyazinkovich.caching.alternative;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

interface Cacheable<T> {

  CompletableFuture<T> getCachedOrLoad(
      String key, Supplier<CompletableFuture<T>> loader);
}
