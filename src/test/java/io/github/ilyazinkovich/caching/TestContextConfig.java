package io.github.ilyazinkovich.caching;

import java.util.concurrent.CompletableFuture;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
class TestContextConfig {

  @Bean
  HttpClient httpClient() {
    return request -> CompletableFuture.supplyAsync(() -> new Response(request.url));
  }

  @Bean
  CachingHttpClient cachingHttpClient(HttpClient httpClient) {
    return new CachingHttpClient(httpClient);
  }

  @Bean
  CacheManager cacheManager() {
    return new ConcurrentMapCacheManager("http");
  }
}
