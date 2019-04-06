package io.github.ilyazinkovich.caching.spring;

import io.github.ilyazinkovich.caching.HttpClient;
import io.github.ilyazinkovich.caching.Request;
import io.github.ilyazinkovich.caching.Response;
import java.util.concurrent.ExecutionException;
import org.springframework.cache.annotation.Cacheable;

public class BlockingCachingHttpClient {

  private final HttpClient httpClient;

  public BlockingCachingHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Cacheable(cacheNames = "http", key = "#request.method + #request.url")
  public Response send(Request request) throws ExecutionException, InterruptedException {
    return httpClient.send(request).get();
  }
}
