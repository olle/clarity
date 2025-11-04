package clarity.management.events;

import clarity.infrastructure.DomainEvent;
import clarity.management.domain.RabbitMqBroker;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

/** Emitted after the fact that a broker entity was updated and persisted. */
public record BrokerUpdatedEvent(
    RabbitMqBroker current, RabbitMqBroker previous, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static BrokerUpdatedEvent from(RabbitMqBroker curr, RabbitMqBroker prev) {
    return new BrokerUpdatedEvent(curr, prev, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
