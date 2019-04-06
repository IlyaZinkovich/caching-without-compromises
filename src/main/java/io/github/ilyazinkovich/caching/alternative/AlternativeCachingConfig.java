package io.github.ilyazinkovich.caching.alternative;

import com.google.gson.Gson;
import io.github.ilyazinkovich.caching.HttpClient;
import io.github.ilyazinkovich.caching.Response;
import io.lettuce.core.api.async.RedisAsyncCommands;
import java.util.concurrent.CompletableFuture;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlternativeCachingConfig {

  @Bean
  CachingHttpClientAlternative cachingHttpClientAlternative(
      RedisAsyncCommands<String, String> redis, Gson gson) {
    HttpClient httpClient = request ->
        CompletableFuture.supplyAsync(() -> new Response(request.url));
    GsonSerializer<Response> serializer =
        new GsonSerializer<>(gson, Response.class);
    RedisCacheable<Response> cacheable = new RedisCacheable<>(redis,
        serializer);
    return new CachingHttpClientAlternative(httpClient, cacheable);
  }
}
