package clarity.brokers;

import clarity.brokers.domain.BrokerType;
import clarity.brokers.domain.RabbitMqBroker;
import clarity.brokers.event.BrokerAddedEvent;
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
