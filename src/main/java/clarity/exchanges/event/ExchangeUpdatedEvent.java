package clarity.exchanges.event;

import clarity.exchanges.RabbitMqExchange;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

public record ExchangeUpdatedEvent(
    RabbitMqExchange current, RabbitMqExchange previous, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static ExchangeUpdatedEvent from(RabbitMqExchange curr, RabbitMqExchange prev) {
    return new ExchangeUpdatedEvent(curr, prev, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
