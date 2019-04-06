package io.github.ilyazinkovich.caching;

import java.util.concurrent.CompletableFuture;

public interface HttpClient {

  CompletableFuture<Response> send(Request request);
}
