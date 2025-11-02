package clarity.brokers.api;

import clarity.brokers.domain.BrokerType;
import clarity.brokers.domain.RabbitMqBroker;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.UUID;

@JsonInclude(Include.NON_EMPTY)
record BrokerDto(
    UUID id,
    String type,
    String host,
    Integer port,
    String username,
    String password,
    boolean ssl,
    String rabbitMqVersion) {

  public static BrokerDto from(RabbitMqBroker broker) {
    return new BrokerDto(
        broker.id(),
        broker.type().toString(),
        broker.properties().host(),
        broker.properties().port(),
        broker.type() == BrokerType.MANAGED ? broker.properties().username() : null,
        broker.type() == BrokerType.MANAGED ? broker.properties().password() : null,
        broker.properties().ssl(),
        broker.properties().rabbitMqVersion());
  }
}
