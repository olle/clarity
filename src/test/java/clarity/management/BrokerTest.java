package clarity.management;

import static org.assertj.core.api.Assertions.assertThat;

import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BrokerTest {

  @Test
  void ensureMutatesProperties() {

    var broker = new RabbitMqBroker(UUID.randomUUID(), BrokerType.MANAGED);

    assertThat(broker).hasFieldOrPropertyWithValue("host", null);
    assertThat(broker).hasFieldOrPropertyWithValue("port", null);
    assertThat(broker).hasFieldOrPropertyWithValue("username", null);
    assertThat(broker).hasFieldOrPropertyWithValue("password", null);

    broker.with("host", 1234, "username", "password");

    assertThat(broker).hasFieldOrPropertyWithValue("host", "host");
    assertThat(broker).hasFieldOrPropertyWithValue("port", 1234);
    assertThat(broker).hasFieldOrPropertyWithValue("username", "username");
    assertThat(broker).hasFieldOrPropertyWithValue("password", "password");
  }
}
