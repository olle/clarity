package clarity.discovery.domain;

import java.io.Serial;
import java.util.concurrent.ConcurrentHashMap;

public class ExchangeAttributes extends ConcurrentHashMap<String, Object> {

  @Serial private static final long serialVersionUID = 1L;

  private ExchangeAttributes() {
    super(3);
  }

  private ExchangeAttributes(ExchangeAttributes other) {}

  public static ExchangeAttributes empty() {
    return new ExchangeAttributes();
  }

  public ExchangeAttributes with(String key, Object value) {
    put(key, value);
    return new ExchangeAttributes(this);
  }
}
