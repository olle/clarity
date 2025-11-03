package clarity.brokers.event;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

public record BrokerActivatedEvent(RabbitMqBroker broker, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static BrokerActivatedEvent from(RabbitMqBroker broker) {
    return new BrokerActivatedEvent(broker, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
