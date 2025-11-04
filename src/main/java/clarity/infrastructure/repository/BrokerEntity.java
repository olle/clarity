package clarity.infrastructure.repository;

import clarity.infrastructure.config.BrokersConfigurationProperty.ConfiguredBroker;
import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import java.util.UUID;

record BrokerEntity(
    UUID id,
    String type,
    String host,
    Integer port,
    String username,
    String password,
    Boolean ssl,
    Integer httpPort,
    String rabbitMqVersion,
    Boolean active,
    Boolean connected) {

  public BrokerEntity(BrokerEntity other) {
    this(
        other.id,
        other.type,
        other.host,
        other.port,
        other.username,
        other.password,
        other.ssl,
        other.httpPort,
        other.rabbitMqVersion,
        other.active,
        other.connected);
  }

  public static BrokerEntity from(RabbitMqBroker rabbitMqBroker) {
    return new BrokerEntity(
        createUuidFrom(rabbitMqBroker.name()),
        rabbitMqBroker.type().name(),
        rabbitMqBroker.properties().host(),
        rabbitMqBroker.properties().port(),
        rabbitMqBroker.properties().username(),
        rabbitMqBroker.properties().password(),
        rabbitMqBroker.properties().ssl(),
        rabbitMqBroker.properties().httpPort(),
        rabbitMqBroker.properties().rabbitMqVersion(),
        rabbitMqBroker.properties().active(),
        rabbitMqBroker.properties().connected());
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
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion,
        this.active,
        this.connected);
  }

  private static UUID createUuidFrom(String username, String host, Integer port) {
    return createUuidFrom("%s@%s:%d".formatted(username, host, port));
  }

  private static UUID createUuidFrom(String name) {
    return UUID.nameUUIDFromBytes(name.getBytes());
  }

  public RabbitMqBroker toModel() {
    return new RabbitMqBroker(this.id, BrokerType.valueOf(this.type))
        .withProperties(
            properties ->
                properties
                    .withHost(host)
                    .withPort(port)
                    .withUsername(username)
                    .withPassword(password)
                    .withSSL(ssl)
                    .withHttpPort(httpPort)
                    .withRabbitMqVersion(rabbitMqVersion)
                    .withActive(active)
                    .withConnected(connected));
  }
}
