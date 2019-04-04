package io.github.ilyazinkovich.caching;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface Cacheable<T> {

  CompletableFuture<T> getCachedOrLoad(String key, Supplier<CompletableFuture<T>> loader);
}
