package io.github.ilyazinkovich.caching.alternative;

import io.github.ilyazinkovich.caching.HttpClient;
import io.github.ilyazinkovich.caching.Request;
import io.github.ilyazinkovich.caching.Response;
import java.util.concurrent.CompletionStage;

public class CachingHttpClientAlternative {

  private final HttpClient httpClient;
  private final Cacheable<Response> cacheable;

  public CachingHttpClientAlternative(
      HttpClient httpClient, Cacheable<Response> cacheable) {
    this.httpClient = httpClient;
    this.cacheable = cacheable;
  }

  public CompletionStage<Response> send(Request request) {
    String key = request.method + request.url;
    return cacheable.getCachedOrLoad(key, () -> httpClient.send(request));
  }
}
