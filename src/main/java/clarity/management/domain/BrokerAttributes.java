package clarity.management.domain;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.lang.Nullable;

public class BrokerAttributes extends ConcurrentHashMap<String, Object> {

  @Serial private static final long serialVersionUID = 1L;

  private BrokerAttributes() {
    super();
  }

  private BrokerAttributes(BrokerAttributes other) {
    super(other);
  }

  private BrokerAttributes(Map<String, Object> map) {
    super(map);
  }

  public static BrokerAttributes empty() {
    return new BrokerAttributes();
  }

  public BrokerAttributes with(String key, Object value) {
    put(key, value);
    return new BrokerAttributes(this);
  }

  public BrokerAttributes without(String key) {
    remove(key);
    return new BrokerAttributes(this);
  }

  public BrokerAttributes withNullable(String key, @Nullable Object maybe) {
    if (maybe != null) return with(key, maybe);
    else return without(key);
  }

  protected BrokerAttributes withAll(Map<String, Object> attributes) {
    putAll(attributes);
    return new BrokerAttributes(this);
  }

  protected BrokerAttributes replace(Map<String, Object> attributes) {
    return new BrokerAttributes(attributes);
  }
}
