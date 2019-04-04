package io.github.ilyazinkovich.caching;

import java.util.Objects;

public class Response {

  private String body;

  public Response() {
  }

  public Response(String body) {
    this.body = body;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Response response = (Response) o;
    return Objects.equals(body, response.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(body);
  }
}
