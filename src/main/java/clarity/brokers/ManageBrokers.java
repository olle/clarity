package clarity.brokers;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageBrokers {

  private final BrokerRepository repo;

  public ManageBrokers(BrokerRepository repo) {
    this.repo = repo;
  }

  public void create(RabbitMqBroker broker) {
    repo.save(broker);
  }

  public void update(RabbitMqBroker broker) {
    repo.save(broker);
  }

  public void delete(UUID id) {
    repo.deleteById(id);
  }

  @Transactional
  public void activate(RabbitMqBroker broker) {

    var activated = broker.activate();
    repo.save(activated);
  }
}
