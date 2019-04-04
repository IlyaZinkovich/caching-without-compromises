package io.github.ilyazinkovich.caching;

import java.util.concurrent.CompletableFuture;

public class CachingHttpClientAlternative {

  private final HttpClient httpClient;
  private final Cacheable<Response> cacheable;

  public CachingHttpClientAlternative(
      HttpClient httpClient, Cacheable<Response> cacheable) {
    this.httpClient = httpClient;
    this.cacheable = cacheable;
  }

  public CompletableFuture<Response> send(Request request) {
    String key = request.method + request.url;
    return cacheable.getCachedOrLoad(key, () -> httpClient.send(request));
  }
}
