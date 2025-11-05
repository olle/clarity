package clarity.management.domain;

import org.springframework.lang.Nullable;

public record BrokerProperties(@Nullable Integer httpPort, Boolean active, Boolean connected) {

  public static BrokerProperties empty() {
    return new BrokerProperties(null, null, null);
  }

  public BrokerProperties withHttpPort(Integer httpPort) {
    return new BrokerProperties(httpPort, this.active, this.connected);
  }

  public BrokerProperties withActive(Boolean active) {
    return new BrokerProperties(this.httpPort, active, this.connected);
  }

  public BrokerProperties withConnected(Boolean connected) {
    return new BrokerProperties(this.httpPort, this.active, connected);
  }

  protected BrokerProperties copy() {
    return new BrokerProperties(this.httpPort, this.active, this.connected);
  }
}
