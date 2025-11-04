package clarity.discovery.events;

import clarity.discovery.domain.RabbitMqExchange;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

public record ExchangeAddedEvent(RabbitMqExchange exchange, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static ExchangeAddedEvent from(RabbitMqExchange exchange) {
    return new ExchangeAddedEvent(exchange, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
