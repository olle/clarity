package clarity.infrastructure.repository;

import clarity.exchanges.RabbitMqExchange;
import java.util.UUID;

public record ExchangeEntity(UUID id, String name) {

  public ExchangeEntity(ExchangeEntity other) {
    this(other.id, other.name);
  }

  public static ExchangeEntity from(RabbitMqExchange exchange) {
    return new ExchangeEntity(createUniqueUUID(), exchange.name());
  }

  private static UUID createUniqueUUID() {
    return UUID.randomUUID();
  }

  public ExchangeEntity withId(UUID id) {
    return new ExchangeEntity(id, this.name);
  }

  public RabbitMqExchange toModel() {
    return new RabbitMqExchange(id, name);
  }
}
