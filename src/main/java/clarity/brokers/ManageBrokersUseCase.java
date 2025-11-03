package clarity.brokers;

import clarity.brokers.api.ManageUseCase;
import clarity.brokers.domain.RabbitMqBroker;
import clarity.brokers.event.RabbitMqBrokerResolvedEvent;
import clarity.infrastructure.utils.Loggable;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ManageBrokersUseCase implements ManageUseCase, Loggable {

  private final BrokerRepository repo;

  public ManageBrokersUseCase(BrokerRepository repo) {
    this.repo = repo;
  }

  @Override
  @Transactional
  public void create(RabbitMqBroker broker) {
    if (!repo.exists(broker)) {
      repo.save(broker);
    }
  }

  @Override
  public void update(RabbitMqBroker broker) {
    repo.save(broker);
  }

  @Override
  public void delete(UUID id) {
    repo.deleteById(id);
  }

  @Async
  @EventListener
  public void on(RabbitMqBrokerResolvedEvent event) {
    repo.save(event.broker());
  }
}
