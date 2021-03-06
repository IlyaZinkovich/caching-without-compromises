package io.github.ilyazinkovich.caching.alternative;

import static io.github.ilyazinkovich.caching.Request.Method.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import com.google.gson.Gson;
import io.github.ilyazinkovich.caching.Request;
import io.github.ilyazinkovich.caching.Response;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.async.RedisAsyncCommands;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

@TestInstance(PER_CLASS)
class CachingHttpClientAlternativeTest {

  private static final String REDIS_HOST = "localhost";
  private static final int REDIS_PORT = 6379;
  private final GenericContainer redisContainer =
      new FixedHostPortGenericContainer("redis")
          .withFixedExposedPort(REDIS_PORT, REDIS_PORT);
  private final Gson gson = new Gson();
  private CachingHttpClientAlternative cachingHttpClient;

  @BeforeAll
  void setup() {
    redisContainer.start();
    RedisURI uri = RedisURI.create(REDIS_HOST, REDIS_PORT);
    RedisAsyncCommands<String, String> redis = RedisClient.create(uri).connect().async();
    cachingHttpClient = new CachingConfig().cachingHttpClientAlternative(redis, gson);
  }

  @Test
  void test() throws ExecutionException, InterruptedException {
    final String url = "http://localhost:8080";
    final Request request = new Request(GET, url);
    final String differentUrl = "http://localhost:8080/different";
    final Request differentRequest = new Request(GET, differentUrl);

    final Response originalResponse = cachingHttpClient.send(request).get();
    final Response cachedResponse = cachingHttpClient.send(request).get();
    final Response differentResponse = cachingHttpClient.send(differentRequest).get();

    assertEquals(originalResponse, cachedResponse);
    assertNotEquals(cachedResponse, differentResponse);
  }

  @AfterAll
  void shutdown() {
    redisContainer.stop();
  }
}
