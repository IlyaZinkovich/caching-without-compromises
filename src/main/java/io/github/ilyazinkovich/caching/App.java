package io.github.ilyazinkovich.caching;

import io.github.ilyazinkovich.caching.Request.Method;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ContextConfig.class)
public class App {

  public static void main(String[] args) {
    final ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    final CachingHttpClient cachingHttpClient = context.getBean(CachingHttpClient.class);
    final Response futureResponse =
        cachingHttpClient.send(new Request(Method.GET, "http://localhost:8080"));
    System.out.println(futureResponse.getBody());
  }
}
