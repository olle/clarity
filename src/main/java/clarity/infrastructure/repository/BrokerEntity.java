package clarity.infrastructure.repository;

import clarity.infrastructure.config.BrokersConfigurationProperty.ConfiguredBroker;
import clarity.infrastructure.utils.Utils;
import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

record BrokerEntity(
    UUID id,
    String type,
    String host,
    Integer port,
    String username,
    String password,
    Integer httpPort,
    Boolean active,
    Boolean connected,
    Map<String, Object> attributes) {

  public BrokerEntity(BrokerEntity other) {
    this(
        other.id,
        other.type,
        other.host,
        other.port,
        other.username,
        other.password,
        other.httpPort,
        other.active,
        other.connected,
        Collections.unmodifiableMap(other.attributes()));
  }

  public static BrokerEntity from(RabbitMqBroker rabbitMqBroker) {
    return new BrokerEntity(
        createUuidFrom(rabbitMqBroker.name()),
        rabbitMqBroker.type().name(),
        rabbitMqBroker.host(),
        rabbitMqBroker.port(),
        rabbitMqBroker.username(),
        rabbitMqBroker.password(),
        rabbitMqBroker.properties().httpPort(),
        rabbitMqBroker.properties().active(),
        rabbitMqBroker.properties().connected(),
        rabbitMqBroker.attributes());
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
        configuredBroker.getHttpPort(),
        null,
        null,
        null);
  }

  public BrokerEntity withId(UUID id) {
    return new BrokerEntity(
        id,
        this.type,
        this.host,
        this.port,
        this.username,
        this.password,
        this.httpPort,
        this.active,
        this.connected,
        this.attributes);
  }

  private static UUID createUuidFrom(String username, String host, Integer port) {
    return createUuidFrom("%s@%s:%d".formatted(username, host, port));
  }

  private static UUID createUuidFrom(String name) {
    return UUID.nameUUIDFromBytes(Utils.toBytes(name));
  }

  public RabbitMqBroker toModel() {
    return new RabbitMqBroker(this.id, BrokerType.valueOf(this.type))
        .with(host, port, username, password)
        .withProperties(
            properties ->
                properties.withHttpPort(httpPort).withActive(active).withConnected(connected))
        .withAttributes(attributes);
  }
}
