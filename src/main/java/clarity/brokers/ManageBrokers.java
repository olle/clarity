package clarity.brokers;

import clarity.brokers.event.RabbitMqBrokerResolvedEvent;
import clarity.infrastructure.utils.Loggable;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageBrokers implements Loggable {

  private final BrokerRepository repo;

  public ManageBrokers(BrokerRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public void create(RabbitMqBroker broker) {
    if (!repo.exists(broker)) {
      repo.save(broker);
    }
  }

  public void update(RabbitMqBroker broker) {
    repo.save(broker);
  }

  public void delete(UUID id) {
    repo.deleteById(id);
  }

  @Async
  @EventListener
  public void on(RabbitMqBrokerResolvedEvent event) {
    repo.save(event.broker());
  }
}
