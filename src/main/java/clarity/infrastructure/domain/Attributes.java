package clarity.infrastructure.domain;

import java.io.Serial;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jspecify.annotations.Nullable;

public class Attributes extends ConcurrentHashMap<String, Object> {

  @Serial private static final long serialVersionUID = 1L;

  private Attributes() {
    super();
  }

  private Attributes(Attributes other) {
    super(other);
  }

  private Attributes(Map<String, Object> map) {
    super(map);
  }

  public static Attributes empty() {
    return new Attributes();
  }

  public Attributes with(String key, Object value) {
    var next = new Attributes(this);
    next.put(key, value);
    return next;
  }

  public Attributes without(String key) {
    var next = new Attributes(this);
    next.remove(key);
    return next;
  }

  public Attributes withNullable(String key, @Nullable Object maybe) {
    if (maybe != null) {
      return with(key, maybe);
    } else {
      return without(key);
    }
  }

  public Attributes withAll(Map<String, Object> attributes) {
    var next = new Attributes(this);
    next.putAll(attributes);
    return next;
  }

  public Attributes replace(Map<String, Object> attributes) {
    return new Attributes(attributes);
  }

  public Map<String, Object> toUnmodifiableMap() {
    return Collections.unmodifiableMap(this);
  }
}
