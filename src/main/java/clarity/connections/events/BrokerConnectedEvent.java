package clarity.connections.events;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

public record BrokerConnectedEvent(RabbitMqBroker broker, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static BrokerConnectedEvent from(RabbitMqBroker broker) {
    return new BrokerConnectedEvent(broker, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
