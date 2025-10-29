package clarity.brokers;

import clarity.exchanges.event.ExchangeResolvedEvent;
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

  @Async
  @EventListener
  public void on(ExchangeResolvedEvent event) {
    logger().warn("NOT YET HANDLING {}", event);
  }
}
