package clarity.brokers.domain;

import org.springframework.lang.Nullable;

public record BrokerProperties(
    String host,
    Integer port,
    String username,
    String password,
    boolean ssl,
    @Nullable Integer httpPort,
    String rabbitMqVersion,
    Boolean active,
    Boolean connected) {

  public static BrokerProperties empty() {
    return new BrokerProperties(null, null, null, null, false, null, null, null, null);
  }

  public BrokerProperties withHost(String host) {
    return new BrokerProperties(
        host,
        this.port,
        this.username,
        this.password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion,
        this.active,
        this.connected);
  }

  public BrokerProperties withPort(Integer port) {
    return new BrokerProperties(
        this.host,
        port,
        this.username,
        this.password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion,
        this.active,
        this.connected);
  }

  public BrokerProperties withUsername(String username) {
    return new BrokerProperties(
        this.host,
        this.port,
        username,
        this.password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion,
        this.active,
        this.connected);
  }

  public BrokerProperties withPassword(String password) {
    return new BrokerProperties(
        this.host,
        this.port,
        this.username,
        password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion,
        this.active,
        this.connected);
  }

  public BrokerProperties withSSL(boolean ssl) {
    return new BrokerProperties(
        this.host,
        this.port,
        this.username,
        password,
        ssl,
        this.httpPort,
        this.rabbitMqVersion,
        this.active,
        this.connected);
  }

  public BrokerProperties withHttpPort(Integer httpPort) {
    return new BrokerProperties(
        this.host,
        this.port,
        this.username,
        password,
        this.ssl,
        httpPort,
        this.rabbitMqVersion,
        this.active,
        this.connected);
  }

  public BrokerProperties withRabbitMqVersion(String rabbitMqVersion) {
    return new BrokerProperties(
        this.host,
        this.port,
        this.username,
        this.password,
        this.ssl,
        this.httpPort,
        rabbitMqVersion,
        this.active,
        this.connected);
  }

  public BrokerProperties withActive(Boolean active) {
    return new BrokerProperties(
        this.host,
        this.port,
        this.username,
        this.password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion,
        active,
        this.connected);
  }

  public BrokerProperties withConnected(Boolean connected) {
    return new BrokerProperties(
        this.host,
        this.port,
        this.username,
        this.password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion,
        this.active,
        connected);
  }

  protected BrokerProperties copy() {
    return new BrokerProperties(
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
}
