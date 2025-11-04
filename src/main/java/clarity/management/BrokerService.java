package clarity.management;

import clarity.infrastructure.utils.Loggable;
import clarity.management.api.ManageBrokers;
import clarity.management.domain.RabbitMqBroker;
import clarity.management.events.BrokerActivatedEvent;
import clarity.management.events.BrokerDeactivatedEvent;
import clarity.management.events.RabbitMqBrokerResolvedEvent;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class BrokerService implements ManageBrokers, Loggable {

  private final BrokerRepository repo;
  private final ApplicationEventPublisher publisher;

  public BrokerService(BrokerRepository repo, ApplicationEventPublisher publisher) {
    this.repo = repo;
    this.publisher = publisher;
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

  @Override
  @Transactional
  public void activate(UUID brokerId) {
    RabbitMqBroker broker =
        repo.findById(brokerId).orElseThrow(() -> new BrokerNotFoundException());
    broker.activate();
    repo.save(broker);
    publisher.publishEvent(BrokerActivatedEvent.from(broker));
  }

  @Override
  @Transactional
  public void deactivate(UUID brokerId) {
    RabbitMqBroker broker =
        repo.findById(brokerId).orElseThrow(() -> new BrokerNotFoundException());
    broker.deactivate();
    repo.save(broker);
    publisher.publishEvent(BrokerDeactivatedEvent.from(broker));
  }
}
