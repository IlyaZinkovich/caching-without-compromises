package io.github.ilyazinkovich.caching;

import static io.github.ilyazinkovich.caching.Request.Method.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextConfig.class)
@TestInstance(PER_CLASS)
class CachingHttpClientTest {

  @Autowired
  private CachingHttpClient cachingHttpClient;

  @Test
  void test() {
    final String url = "http://localhost:8080";
    final Request request = new Request(GET, url);
    final Response response = cachingHttpClient.send(request);
    assertEquals(new Response(url), response);
    final Response cachedResponse = cachingHttpClient.send(request);
    assertEquals(response, cachedResponse);
  }
}
