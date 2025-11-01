package clarity.brokers.event;

import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

public record BrokerMarkedRemovedEvent(UUID removedBrokerId, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static BrokerMarkedRemovedEvent from(UUID brokerId) {
    return new BrokerMarkedRemovedEvent(brokerId, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
