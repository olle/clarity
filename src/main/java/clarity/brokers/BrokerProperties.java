package clarity.brokers;

import org.springframework.lang.Nullable;

public record BrokerProperties(
    String host,
    Integer port,
    String username,
    String password,
    boolean ssl,
    @Nullable Integer httpPort) {

  public static BrokerProperties empty() {
    return new BrokerProperties(null, null, null, null, false, null);
  }

  protected BrokerProperties copy() {
    return new BrokerProperties(
        this.host, this.port, this.username, this.password, this.ssl, this.httpPort);
  }

  public BrokerProperties withHost(String host) {
    return new BrokerProperties(
        host, this.port, this.username, this.password, this.ssl, this.httpPort);
  }

  public BrokerProperties withPort(Integer port) {
    return new BrokerProperties(
        this.host, port, this.username, this.password, this.ssl, this.httpPort);
  }

  public BrokerProperties withUsername(String username) {
    return new BrokerProperties(
        this.host, this.port, username, this.password, this.ssl, this.httpPort);
  }

  public BrokerProperties withPassword(String password) {
    return new BrokerProperties(
        this.host, this.port, this.username, password, this.ssl, this.httpPort);
  }

  public BrokerProperties withSSL(boolean ssl) {
    return new BrokerProperties(this.host, this.port, this.username, password, ssl, this.httpPort);
  }

  public BrokerProperties withHttpPort(Integer httpPort) {
    return new BrokerProperties(this.host, this.port, this.username, password, this.ssl, httpPort);
  }
}
