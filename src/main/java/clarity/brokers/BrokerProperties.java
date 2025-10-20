package clarity.brokers;

public record BrokerProperties(
    String host, Integer port, String username, String password, boolean ssl) {

  public static BrokerProperties empty() {
    return new BrokerProperties(null, null, null, null, false);
  }

  public BrokerProperties withHost(String host) {
    return new BrokerProperties(host, this.port, this.username, this.password, this.ssl);
  }

  public BrokerProperties withPort(int port) {
    return new BrokerProperties(this.host, port, this.username, this.password, this.ssl);
  }

  public BrokerProperties withUsername(String username) {
    return new BrokerProperties(this.host, this.port, username, this.password, this.ssl);
  }

  public BrokerProperties withPassword(String password) {
    return new BrokerProperties(this.host, this.port, this.username, password, this.ssl);
  }

  public BrokerProperties withSSL(boolean ssl) {
    return new BrokerProperties(this.host, this.port, this.username, password, ssl);
  }
}
