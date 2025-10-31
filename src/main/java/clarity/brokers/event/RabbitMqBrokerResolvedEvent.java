package clarity.brokers.event;

import clarity.brokers.RabbitMqBroker;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

public record RabbitMqBrokerResolvedEvent(RabbitMqBroker broker, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static RabbitMqBrokerResolvedEvent from(RabbitMqBroker rabbitMqBroker) {
    return new RabbitMqBrokerResolvedEvent(
        rabbitMqBroker, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
