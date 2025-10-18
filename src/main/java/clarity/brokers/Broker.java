package clarity.brokers;

import java.util.UUID;

public class Broker {

  public final UUID id;

  public Broker(UUID id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Broker [id=" + id + "]";
  }
}
