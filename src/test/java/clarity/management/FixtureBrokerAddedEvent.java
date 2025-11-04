package clarity.management;

import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import clarity.management.events.BrokerAddedEvent;
import java.time.Instant;
import java.util.UUID;

public class FixtureBrokerAddedEvent {

  private BrokerAddedEvent fixture;

  public FixtureBrokerAddedEvent(BrokerAddedEvent value) {
    this.fixture = value;
  }

  public static FixtureBrokerAddedEvent random() {
    return new FixtureBrokerAddedEvent(
        new BrokerAddedEvent(
            new RabbitMqBroker(UUID.randomUUID(), BrokerType.MANAGED),
            UUID.randomUUID(),
            Instant.now()));
  }

  public BrokerAddedEvent get() {
    return fixture;
  }
}
