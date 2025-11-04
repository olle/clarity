package clarity.connectivity.events;

import clarity.management.domain.RabbitMqBroker;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

public record BrokerDisconnectedEvent(RabbitMqBroker broker, UUID uuid, Instant timestamp) {

  public static BrokerDisconnectedEvent from(RabbitMqBroker broker) {
    return new BrokerDisconnectedEvent(broker, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
