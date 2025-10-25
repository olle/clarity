package clarity.infrastructure;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

  UUID uuid();

  Instant timestamp();
}
