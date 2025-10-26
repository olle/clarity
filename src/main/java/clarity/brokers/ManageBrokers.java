package clarity.brokers;

import java.util.UUID;
import org.springframework.stereotype.Service;

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

  public void activate(RabbitMqBroker broker) {
    // TODO Auto-generated method stub
  }
}
