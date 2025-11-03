package clarity.brokers;

import clarity.brokers.api.ActivateUseCase;
import clarity.brokers.domain.RabbitMqBroker;
import clarity.brokers.event.BrokerActivatedEvent;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class ActivateClarityForBrokerUseCase implements ActivateUseCase {

  private final BrokerRepository repo;
  private final ApplicationEventPublisher publisher;

  public ActivateClarityForBrokerUseCase(
      BrokerRepository repo, ApplicationEventPublisher publisher) {
    this.repo = repo;
    this.publisher = publisher;
  }

  @Transactional
  public void execute(UUID brokerId) {

    RabbitMqBroker broker =
        repo.findById(brokerId).orElseThrow(() -> new BrokerNotFoundException());
    broker.activate();
    repo.save(broker);
    publisher.publishEvent(BrokerActivatedEvent.from(broker));
  }
}
