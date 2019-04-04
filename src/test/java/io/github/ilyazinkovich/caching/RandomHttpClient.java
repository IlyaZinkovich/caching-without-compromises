package io.github.ilyazinkovich.caching;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class RandomHttpClient implements HttpClient {

  @Override
  public CompletableFuture<Response> send(final Request request) {
    final String randomString = UUID.randomUUID().toString();
    final Response response = new Response(randomString);
    return CompletableFuture.completedFuture(response);
  }
}
