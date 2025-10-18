package clarity.brokers;

import clarity.brokers.BrokerRepository.BrokerEntity;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

/** Emitted after the fact that a broker entity was updated and persisted. */
public record BrokerUpdatedEvent(Broker current, Broker previous, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static BrokerUpdatedEvent from(BrokerEntity curr, BrokerEntity prev) {
    return new BrokerUpdatedEvent(
        curr.toModel(), prev.toModel(), UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
