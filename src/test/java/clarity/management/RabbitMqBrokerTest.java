package clarity.management;

import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RabbitMqBrokerTest {

  @Test
  void showToString() {
    System.out.println(new RabbitMqBroker(UUID.randomUUID(), BrokerType.MANAGED));
  }
}
