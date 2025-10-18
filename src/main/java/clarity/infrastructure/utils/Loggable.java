package clarity.infrastructure.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Logging trait. */
public interface Loggable {

  default Logger logger() {
    return LoggerFactory.getLogger(getClass());
  }
}
