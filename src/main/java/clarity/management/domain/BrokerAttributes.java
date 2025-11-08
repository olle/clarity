package clarity.management.domain;

import java.io.Serial;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.lang.Nullable;

public class BrokerAttributes extends ConcurrentHashMap<String, Object> {

  @Serial private static final long serialVersionUID = 1L;

  public BrokerAttributes with(String key, Object value) {
    put(key, value);
    return this;
  }

  public BrokerAttributes without(String key) {
    remove(key);
    return this;
  }

  public BrokerAttributes withNullable(String key, @Nullable Object maybe) {
    if (maybe != null) return with(key, maybe);
    else return without(key);
  }
}
