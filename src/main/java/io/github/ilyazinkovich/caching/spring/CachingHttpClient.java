package io.github.ilyazinkovich.caching.spring;

import io.github.ilyazinkovich.caching.HttpClient;
import io.github.ilyazinkovich.caching.Request;
import io.github.ilyazinkovich.caching.Response;
import java.util.concurrent.CompletableFuture;
import org.springframework.cache.annotation.Cacheable;

public class CachingHttpClient {

  private final HttpClient httpClient;

  public CachingHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Cacheable(cacheNames = "http", key = "#request.method + #request.url")
  public CompletableFuture<Response> send(Request request) {
    return httpClient.send(request);
  }
}

