package clarity.management.domain;

import org.springframework.lang.Nullable;

public record BrokerProperties(
    @Nullable Integer httpPort, String rabbitMqVersion, Boolean active, Boolean connected) {

  public static BrokerProperties empty() {
    return new BrokerProperties(null, null, null, null);
  }

  public BrokerProperties withHttpPort(Integer httpPort) {
    return new BrokerProperties(httpPort, this.rabbitMqVersion, this.active, this.connected);
  }

  public BrokerProperties withRabbitMqVersion(String rabbitMqVersion) {
    return new BrokerProperties(this.httpPort, rabbitMqVersion, this.active, this.connected);
  }

  public BrokerProperties withActive(Boolean active) {
    return new BrokerProperties(this.httpPort, this.rabbitMqVersion, active, this.connected);
  }

  public BrokerProperties withConnected(Boolean connected) {
    return new BrokerProperties(this.httpPort, this.rabbitMqVersion, this.active, connected);
  }

  protected BrokerProperties copy() {
    return new BrokerProperties(this.httpPort, this.rabbitMqVersion, this.active, this.connected);
  }
}
