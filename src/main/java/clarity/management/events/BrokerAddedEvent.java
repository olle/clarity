package clarity.management.events;

import clarity.infrastructure.DomainEvent;
import clarity.management.domain.RabbitMqBroker;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

/** Emitted after the fact that a new broker was added and saved to the repository. */
public record BrokerAddedEvent(RabbitMqBroker broker, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static BrokerAddedEvent from(RabbitMqBroker added) {
    return new BrokerAddedEvent(added, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
