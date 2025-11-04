package clarity.management;

import static org.assertj.core.api.Assertions.assertThat;

import clarity.infrastructure.repository.InMemoryBrokerRepository;
import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class ManageBrokersTest {

  @Test
  void createForExistingIsIgnored() {

    var events = new ArrayList<>();
    var repo = new InMemoryBrokerRepository(events::add);

    RabbitMqBroker b1 = brokerFixture();
    RabbitMqBroker b2 = brokerFixture();
    assertThat(b1).isNotSameAs(b2);

    var sut = new BrokerService(repo, null);
    assertThat(events).isEmpty();

    sut.create(b1);
    assertThat(events).hasSize(1);

    sut.create(b2);
    assertThat(events).hasSize(1);
  }

  private RabbitMqBroker brokerFixture() {
    return new RabbitMqBroker(null, BrokerType.MANAGED).with("host", 1234, "user");
  }
}
