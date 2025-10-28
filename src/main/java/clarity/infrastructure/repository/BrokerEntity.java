package clarity.infrastructure.repository;

import clarity.brokers.BrokerType;
import clarity.brokers.BrokersConfigurationProperty.ConfiguredBroker;
import clarity.brokers.RabbitMqBroker;
import java.util.UUID;

record BrokerEntity(
    UUID id,
    String type,
    String host,
    Integer port,
    String username,
    String password,
    Boolean ssl,
    Boolean active) {

  public BrokerEntity(BrokerEntity other) {
    this(
        other.id,
        other.type,
        other.host,
        other.port,
        other.username,
        other.password,
        other.ssl,
        other.active);
  }

  public static BrokerEntity from(RabbitMqBroker rabbitMqBroker) {
    return new BrokerEntity(
        createUuidFrom(
            rabbitMqBroker.properties().username(),
            rabbitMqBroker.properties().host(),
            rabbitMqBroker.properties().port()),
        rabbitMqBroker.type().name(),
        rabbitMqBroker.properties().host(),
        rabbitMqBroker.properties().port(),
        rabbitMqBroker.properties().username(),
        rabbitMqBroker.properties().password(),
        rabbitMqBroker.properties().ssl(),
        rabbitMqBroker.active());
  }

  public static BrokerEntity from(ConfiguredBroker configuredBroker) {
    return new BrokerEntity(
        createUuidFrom(
            configuredBroker.getUsername(), configuredBroker.getHost(), configuredBroker.getPort()),
        BrokerType.CONFIGURED.name(),
        configuredBroker.getHost(),
        configuredBroker.getPort(),
        configuredBroker.getUsername(),
        configuredBroker.getPassword(),
        configuredBroker.isSsl(),
        false);
  }

  public BrokerEntity withId(UUID id) {
    return new BrokerEntity(
        id, this.type, this.host, this.port, this.username, this.password, this.ssl, this.active);
  }

  private static UUID createUuidFrom(String username, String host, Integer port) {
    return UUID.nameUUIDFromBytes("%s@%s:%d".formatted(username, host, port).getBytes());
  }

  public RabbitMqBroker toModel() {
    return new RabbitMqBroker(this.id, BrokerType.valueOf(this.type), this.active)
        .withProperties(
            properties ->
                properties
                    .withHost(host)
                    .withPort(port)
                    .withUsername(username)
                    .withPassword(password)
                    .withSSL(ssl));
  }
}
