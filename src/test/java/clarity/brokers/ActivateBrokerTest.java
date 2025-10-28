package clarity.brokers;

import static org.assertj.core.api.Assertions.assertThat;

import clarity.infrastructure.repository.InMemoryBrokerRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ActivateBrokerTest {

  @Test
  void activatesAndPersistsBroker() {

    var repo = new InMemoryBrokerRepository(_ -> {});
    var sut = new ManageBrokers(repo);

    var broker = new RabbitMqBroker(UUID.randomUUID(), BrokerType.MANAGED);
    assertThat(broker.active()).isFalse();

    repo.save(broker);
    sut.activate(broker);

    var active = repo.findById(broker.id()).get();
    assertThat(active.active()).isTrue();
  }
}
