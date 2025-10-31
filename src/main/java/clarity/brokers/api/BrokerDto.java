package clarity.brokers.api;

import clarity.brokers.BrokerType;
import clarity.brokers.RabbitMqBroker;
import java.util.UUID;
import org.springframework.lang.Nullable;

record BrokerDto(
    @Nullable UUID id,
    @Nullable String type,
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
        broker.properties().username(),
        broker.properties().password(),
        broker.properties().ssl(),
        broker.properties().rabbitMqVersion());
  }

  public RabbitMqBroker toRabbitMqBroker() {
    return toRabbitMqBroker(BrokerType.valueOf(type));
  }

  public RabbitMqBroker toRabbitMqBroker(BrokerType type) {
    return new RabbitMqBroker(id, type)
        .withProperties(
            mapper ->
                mapper
                    .withHost(host)
                    .withPort(port)
                    .withUsername(username)
                    .withPassword(password)
                    .withSSL(ssl));
  }
}
