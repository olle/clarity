package clarity.management.domain;

public record BrokerProperties(Boolean active, Boolean connected) {

  public static BrokerProperties empty() {
    return new BrokerProperties(null, null);
  }

  public BrokerProperties withActive(Boolean active) {
    return new BrokerProperties(active, this.connected);
  }

  public BrokerProperties withConnected(Boolean connected) {
    return new BrokerProperties(this.active, connected);
  }

  protected BrokerProperties copy() {
    return new BrokerProperties(this.active, this.connected);
  }
}
