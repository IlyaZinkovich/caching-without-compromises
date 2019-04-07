package io.github.ilyazinkovich.caching.alternative;

import com.google.gson.Gson;
import io.github.ilyazinkovich.caching.HttpClient;
import io.github.ilyazinkovich.caching.RandomHttpClient;
import io.github.ilyazinkovich.caching.Response;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CachingConfig {

  @Bean
  CachingHttpClientAlternative cachingHttpClientAlternative(
      RedisAsyncCommands<String, String> redis, Gson gson) {
    String cacheName = "http";
    GsonSerializer<Response> serializer = new GsonSerializer<>(gson, Response.class);
    RedisCacheable<Response> cacheable = new RedisCacheable<>(redis, serializer, cacheName);
    HttpClient httpClient = new RandomHttpClient();
    return new CachingHttpClientAlternative(httpClient, cacheable);
  }
}
