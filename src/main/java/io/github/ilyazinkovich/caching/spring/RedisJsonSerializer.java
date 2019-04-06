package io.github.ilyazinkovich.caching.spring;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import java.io.IOException;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class RedisJsonSerializer implements RedisSerializer<Object> {

  private ObjectMapper mapper;

  public RedisJsonSerializer(final ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public byte[] serialize(final Object o) throws SerializationException {
    mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
    try {
      return mapper.writeValueAsBytes(o);
    } catch (JsonProcessingException e) {
      throw new SerializationException("Unable to serialize to JSON.", e);
    }
  }

  @Override
  public Object deserialize(final byte[] bytes) throws SerializationException {
    try {
      return mapper.readValue(bytes, Object.class);
    } catch (IOException e) {
      throw new SerializationException("", e);
    }
  }
}
