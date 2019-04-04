package io.github.ilyazinkovich.caching;

import static io.github.ilyazinkovich.caching.Request.Method.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = InMemoryCacheConfig.class)
class CachingHttpClientInMemoryTest {

  @Autowired
  private CachingHttpClient cachingHttpClient;

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
}
