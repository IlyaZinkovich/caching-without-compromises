package io.github.ilyazinkovich.caching;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public interface Cacheable<T> {

  CompletionStage<T> getCachedOrLoad(String key, Supplier<CompletionStage<T>> loader);
}
