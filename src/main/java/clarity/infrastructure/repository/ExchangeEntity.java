package clarity.infrastructure.repository;

import clarity.exchanges.RabbitMqExchange;
import java.util.UUID;

public record ExchangeEntity(UUID id, String name, String type, UUID brokerId) {

  public ExchangeEntity(ExchangeEntity other) {
    this(other.id, other.name, other.type, other.brokerId);
  }

  public static ExchangeEntity from(RabbitMqExchange exchange) {

    UUID id =
        createUniqueUUIDFrom(exchange.brokerId(), exchange.name(), exchange.properties().type());
    return new ExchangeEntity(
        id, exchange.name(), exchange.properties().type(), exchange.brokerId());
  }

  private static UUID createUniqueUUIDFrom(UUID brokerId, String name, String type) {
    return UUID.nameUUIDFromBytes(
        "%s[%s]@%s".formatted(name, type, brokerId.toString()).getBytes());
  }

  public ExchangeEntity withId(UUID id) {
    return new ExchangeEntity(id, this.name, this.type, this.brokerId);
  }

  public RabbitMqExchange toModel() {
    return new RabbitMqExchange(id, name)
        .withBrokerId(brokerId)
        .withProperties(props -> props.withType(type));
  }
}
