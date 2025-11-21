package clarity.infrastructure.repository;

import clarity.discovery.domain.RabbitMqExchange;
import clarity.infrastructure.utils.Utils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import java.util.UUID;

@SuppressFBWarnings({"EI_EXPOSE_REP"})
public record ExchangeEntity(
    UUID id, String name, String type, UUID brokerId, Map<String, Object> attributes) {

  public ExchangeEntity(ExchangeEntity other) {
    this(other.id, other.name, other.type, other.brokerId, other.attributes());
  }

  public static ExchangeEntity from(RabbitMqExchange exchange) {

    UUID id =
        createUniqueUUIDFrom(exchange.brokerId(), exchange.name(), exchange.properties().type());

    return new ExchangeEntity(
        id,
        exchange.name(),
        exchange.properties().type(),
        exchange.brokerId(),
        exchange.attributes());
  }

  private static UUID createUniqueUUIDFrom(UUID brokerId, String name, String type) {
    return UUID.nameUUIDFromBytes(
        Utils.toBytes("%s[%s]@%s".formatted(name, type, brokerId.toString())));
  }

  public ExchangeEntity withId(UUID id) {
    return new ExchangeEntity(id, this.name, this.type, this.brokerId, this.attributes);
  }

  public RabbitMqExchange toModel() {
    return new RabbitMqExchange(id, name)
        .withBrokerId(brokerId)
        .withProperties(props -> props.withType(type))
        .withAttributes(attributes);
  }
}
