package clarity.infrastructure.domain;

import java.io.Serial;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.lang.Nullable;

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
    put(key, value);
    return new Attributes(this);
  }

  public Attributes without(String key) {
    remove(key);
    return new Attributes(this);
  }

  public Attributes withNullable(String key, @Nullable Object maybe) {
    if (maybe != null) {
      return with(key, maybe);
    } else {
      return without(key);
    }
  }

  public Attributes withAll(Map<String, Object> attributes) {
    putAll(attributes);
    return new Attributes(this);
  }

  public Attributes replace(Map<String, Object> attributes) {
    return new Attributes(attributes);
  }

  public Map<String, Object> toMap() {
    return Collections.unmodifiableMap(this);
  }
}
