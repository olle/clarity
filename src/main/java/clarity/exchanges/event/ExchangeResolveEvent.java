package clarity.exchanges.event;

import clarity.brokers.RabbitMqBroker;
import clarity.exchanges.RabbitMqExchange;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

/**
 * Emitted after the fact that an exchange for some broker was resolved, after being looked up via a
 * remote API call or _sniffing_.
 */
public record ExchangeResolveEvent(
    RabbitMqExchange exchange, RabbitMqBroker broker, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static Object from(RabbitMqExchange exchange, RabbitMqBroker broker) {
    return new ExchangeResolveEvent(
        exchange, broker, UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
