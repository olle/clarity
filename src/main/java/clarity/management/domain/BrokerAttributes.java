package clarity.management.domain;

import java.io.Serial;
import java.util.concurrent.ConcurrentHashMap;

public class BrokerAttributes extends ConcurrentHashMap<String, Object> {

  @Serial private static final long serialVersionUID = 1L;

  public BrokerAttributes with(String key, Object value) {
    put(key, value);
    return this;
  }
}
