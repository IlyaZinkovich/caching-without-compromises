package io.github.ilyazinkovich.caching.spring;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import io.github.ilyazinkovich.caching.HttpClient;
import io.github.ilyazinkovich.caching.Response;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
class ContextConfig {

  @Bean
  HttpClient httpClient() {
    return request -> CompletableFuture.supplyAsync(() -> new Response(request.url));
  }

  @Bean
  CachingHttpClient cachingHttpClient(HttpClient httpClient) {
    return new CachingHttpClient(httpClient);
  }

  @Bean
  RedisConnectionFactory redisConnectionFactory(
      @Value("${spring.redis.host:localhost}") String host,
      @Value("${spring.redis.port:6379}") String port) {
    return new LettuceConnectionFactory(host, Integer.parseInt(port));
  }

  @Bean
  RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    final ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
    RedisSerializer jacksonSerializer = new GenericJackson2JsonRedisSerializer(mapper);
    return new RedisCacheManager(
        RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(SerializationPair.fromSerializer(stringSerializer))
            .serializeValuesWith(SerializationPair.fromSerializer(jacksonSerializer))
    );
  }
}
