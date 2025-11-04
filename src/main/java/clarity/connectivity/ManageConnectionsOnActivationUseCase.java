package clarity.connectivity;

import clarity.connectivity.events.BrokerConnectedEvent;
import clarity.connectivity.events.BrokerDisconnectedEvent;
import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import clarity.management.BrokerRepository;
import clarity.management.events.BrokerActivatedEvent;
import clarity.management.events.BrokerDeactivatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ManageConnectionsOnActivationUseCase implements UseCase, Loggable {

  private final Connections connections;
  private final BrokerRepository brokerRepository;

  public ManageConnectionsOnActivationUseCase(
      Connections connections, BrokerRepository brokerRepository) {
    this.connections = connections;
    this.brokerRepository = brokerRepository;
  }

  @EventListener
  public void on(BrokerActivatedEvent event) {
    connections.connect(event.broker());
  }

  @EventListener
  public void on(BrokerDeactivatedEvent event) {
    connections.disconnect(event.broker());
  }

  @Async
  @EventListener
  @Transactional
  public void on(BrokerConnectedEvent event) {
    brokerRepository
        .findById(event.broker().id())
        .ifPresent(
            broker -> {
              broker.connected();
              brokerRepository.save(broker);
            });
  }

  @Async
  @EventListener
  @Transactional
  public void on(BrokerDisconnectedEvent event) {
    brokerRepository
        .findById(event.broker().id())
        .ifPresent(
            broker -> {
              broker.disconnected();
              brokerRepository.save(broker);
            });
  }
}
