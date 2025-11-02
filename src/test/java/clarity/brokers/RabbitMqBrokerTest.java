package clarity.brokers;

import clarity.brokers.domain.BrokerType;
import clarity.brokers.domain.RabbitMqBroker;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RabbitMqBrokerTest {

  @Test
  void showToString() {
    System.out.println(new RabbitMqBroker(UUID.randomUUID(), BrokerType.MANAGED));
  }
}
