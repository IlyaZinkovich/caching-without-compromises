package io.github.ilyazinkovich.caching;

import io.github.ilyazinkovich.caching.spring.CachingHttpClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
class InMemoryCacheConfig {

  @Bean
  CachingHttpClient cachingHttpClient() {
    final HttpClient httpClient = new RandomHttpClient();
    return new CachingHttpClient(httpClient);
  }

  @Bean
  CacheManager cacheManager() {
    return new ConcurrentMapCacheManager("http");
  }
}
