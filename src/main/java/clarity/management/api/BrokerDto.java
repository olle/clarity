package clarity.management.api;

import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import java.util.UUID;

@JsonInclude(Include.NON_EMPTY)
record BrokerDto(
    UUID id,
    String type,
    String name,
    String host,
    Integer port,
    String username,
    String password,
    Boolean active,
    Boolean connected,
    Map<String, Object> properties) {

  public static BrokerDto from(RabbitMqBroker broker) {
    return new BrokerDto(
        broker.id(),
        broker.type().toString(),
        broker.name(),
        broker.host(),
        broker.port(),
        broker.type() == BrokerType.MANAGED ? broker.username() : null,
        broker.type() == BrokerType.MANAGED ? broker.password() : null,
        broker.properties().active(),
        broker.properties().connected(),
        Map.of());
  }
}
