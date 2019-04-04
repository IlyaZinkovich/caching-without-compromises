package io.github.ilyazinkovich.caching;

import java.util.concurrent.CompletableFuture;
import org.springframework.cache.annotation.Cacheable;

class CachingHttpClient {

  private final HttpClient httpClient;

  CachingHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Cacheable(cacheNames = "http", key = "#request.method + #request.url")
  public CompletableFuture<Response> send(Request request) {
    return httpClient.send(request);
  }
}

