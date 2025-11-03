package clarity.brokers.event;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

public record BrokerDeactivatedEvent(RabbitMqBroker broker, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static BrokerDeactivatedEvent from(RabbitMqBroker broker) {
    return new BrokerDeactivatedEvent(broker, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
