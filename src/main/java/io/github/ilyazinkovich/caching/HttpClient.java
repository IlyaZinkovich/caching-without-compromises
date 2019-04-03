package io.github.ilyazinkovich.caching;

import java.util.concurrent.CompletableFuture;

interface HttpClient {

  CompletableFuture<Response> send(Request request);
}
