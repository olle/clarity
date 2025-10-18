package clarity.brokers;

import clarity.brokers.BrokerRepository.BrokerEntity;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

/** Emitted after the fact that a broker was removed, and deleted from the repository. */
public record BrokerRemovedEvent(Broker removed, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static BrokerRemovedEvent from(BrokerEntity removed) {
    return new BrokerRemovedEvent(
        removed.toModel(), UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
