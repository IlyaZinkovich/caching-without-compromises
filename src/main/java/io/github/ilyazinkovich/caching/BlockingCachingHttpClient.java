package io.github.ilyazinkovich.caching;

import java.util.concurrent.ExecutionException;
import org.springframework.cache.annotation.Cacheable;

class BlockingCachingHttpClient {

  private final HttpClient httpClient;

  BlockingCachingHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Cacheable(cacheNames = "http", key = "#request.method + #request.url")
  public Response send(Request request) throws ExecutionException, InterruptedException {
    return httpClient.send(request).get();
  }
}
