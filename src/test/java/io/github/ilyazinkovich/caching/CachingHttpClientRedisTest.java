package io.github.ilyazinkovich.caching;

import static io.github.ilyazinkovich.caching.Request.Method.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.ilyazinkovich.caching.spring.BlockingCachingHttpClient;
import io.github.ilyazinkovich.caching.spring.CachingHttpClient;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RedisCacheConfig.class)
@TestInstance(Lifecycle.PER_CLASS)
class CachingHttpClientRedisTest {

  GenericContainer redis = new FixedHostPortGenericContainer("redis")
      .withFixedExposedPort(6379, 6379);
  @Autowired
  private CachingHttpClient cachingHttpClient;
  @Autowired
  private BlockingCachingHttpClient blockingCachingHttpClient;

  @BeforeAll
  void setup() {
    redis.start();
  }

  @Test
  void test() {
    final String url = "http://localhost:8080";
    final Request request = new Request(GET, url);

    assertThrows(RuntimeException.class, () -> {
      final Response originalResponse = cachingHttpClient.send(request).get();
      final Response cachedResponse = cachingHttpClient.send(request).get();
    });
  }

  @Test
  void blockingTest() throws ExecutionException, InterruptedException {
    final String url = "http://localhost:8080";
    final Request request = new Request(GET, url);
    final String differentUrl = "http://localhost:8080/different";
    final Request differentRequest = new Request(GET, differentUrl);

    final Response originalResponse = blockingCachingHttpClient.send(request);
    final Response cachedResponse = blockingCachingHttpClient.send(request);
    final Response differentResponse = blockingCachingHttpClient.send(differentRequest);

    assertEquals(originalResponse, cachedResponse);
    assertNotEquals(cachedResponse, differentResponse);
  }

  @AfterAll
  void shutdown() {
    redis.stop();
  }
}
