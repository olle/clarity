package clarity.brokers;

import org.springframework.lang.Nullable;

public record BrokerProperties(
    String host,
    Integer port,
    String username,
    String password,
    boolean ssl,
    @Nullable Integer httpPort,
    String rabbitMqVersion) {

  public static BrokerProperties empty() {
    return new BrokerProperties(null, null, null, null, false, null, null);
  }

  public BrokerProperties withHost(String host) {
    return new BrokerProperties(
        host,
        this.port,
        this.username,
        this.password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion);
  }

  public BrokerProperties withPort(Integer port) {
    return new BrokerProperties(
        this.host,
        port,
        this.username,
        this.password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion);
  }

  public BrokerProperties withUsername(String username) {
    return new BrokerProperties(
        this.host,
        this.port,
        username,
        this.password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion);
  }

  public BrokerProperties withPassword(String password) {
    return new BrokerProperties(
        this.host,
        this.port,
        this.username,
        password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion);
  }

  public BrokerProperties withSSL(boolean ssl) {
    return new BrokerProperties(
        this.host, this.port, this.username, password, ssl, this.httpPort, this.rabbitMqVersion);
  }

  public BrokerProperties withHttpPort(Integer httpPort) {
    return new BrokerProperties(
        this.host, this.port, this.username, password, this.ssl, httpPort, this.rabbitMqVersion);
  }

  public BrokerProperties withRabbitMqVersion(String rabbitMqVersion) {
    return new BrokerProperties(
        this.host,
        this.port,
        this.username,
        this.password,
        this.ssl,
        this.httpPort,
        rabbitMqVersion);
  }

  protected BrokerProperties copy() {
    return new BrokerProperties(
        this.host,
        this.port,
        this.username,
        this.password,
        this.ssl,
        this.httpPort,
        this.rabbitMqVersion);
  }
}
