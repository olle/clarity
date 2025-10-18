package clarity.brokers;

import clarity.brokers.BrokerRepository.BrokerEntity;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

/** Emitted after the fact that a new broker was added and saved to the repository. */
public record BrokerAddedEvent(Broker broker, UUID uuid, Instant timestamp) implements DomainEvent {

  public static BrokerAddedEvent from(BrokerEntity added) {
    return new BrokerAddedEvent(added.toModel(), UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
