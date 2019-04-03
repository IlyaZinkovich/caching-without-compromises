package io.github.ilyazinkovich.caching;

import static java.util.concurrent.TimeUnit.SECONDS;

import org.springframework.cache.annotation.Cacheable;

class CachingHttpClient {

  private final HttpClient httpClient;

  CachingHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Cacheable(cacheNames = "http", key = "#request.method + #request.url")
  public Response send(Request request) {
    try {
      return httpClient.send(request).get(2, SECONDS);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

