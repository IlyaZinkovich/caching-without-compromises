package io.github.ilyazinkovich.caching;

public class Request {

  public final Method method;
  public final String url;

  public Request(final Method method, final String url) {
    this.method = method;
    this.url = url;
  }

  public String hash() {
    return method.name() + url;
  }

  enum Method {
    GET
  }
}
