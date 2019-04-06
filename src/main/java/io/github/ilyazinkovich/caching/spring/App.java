package io.github.ilyazinkovich.caching.spring;

import io.github.ilyazinkovich.caching.Request;
import io.github.ilyazinkovich.caching.Request.Method;
import io.github.ilyazinkovich.caching.Response;
import java.util.concurrent.ExecutionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ContextConfig.class)
public class App {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    final ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    final CachingHttpClient cachingHttpClient = context.getBean(CachingHttpClient.class);
    final Response futureResponse =
        cachingHttpClient.send(new Request(Method.GET, "http://localhost:8080")).get();
    System.out.println(futureResponse.getBody());
  }
}
