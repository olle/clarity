package clarity.brokers;

import java.time.Instant;
import java.util.UUID;

public class FixtureBrokerAddedEvent {

  private BrokerAddedEvent fixture;

  public FixtureBrokerAddedEvent(BrokerAddedEvent value) {
    this.fixture = value;
  }

  public static FixtureBrokerAddedEvent random() {
    return new FixtureBrokerAddedEvent(
        new BrokerAddedEvent(new Broker(UUID.randomUUID()), UUID.randomUUID(), Instant.now()));
  }

  public BrokerAddedEvent get() {
    return fixture;
  }
}
