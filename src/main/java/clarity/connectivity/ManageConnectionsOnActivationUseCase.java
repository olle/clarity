package clarity.connectivity;

import clarity.connectivity.events.BrokerConnectedEvent;
import clarity.connectivity.events.BrokerDisconnectedEvent;
import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import clarity.management.BrokerRepository;
import clarity.management.events.BrokerActivatedEvent;
import clarity.management.events.BrokerDeactivatedEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ManageConnectionsOnActivationUseCase implements UseCase, Loggable {

  private final Connections connections;
  private final BrokerRepository brokerRepository;

  public Map<UUID, Integer> activated = new ConcurrentHashMap<>();

  public ManageConnectionsOnActivationUseCase(
      Connections connections, BrokerRepository brokerRepository) {
    this.connections = connections;
    this.brokerRepository = brokerRepository;
  }

  @EventListener
  public void on(BrokerActivatedEvent event) {
    synchronized (activated) {
      int count = activated.getOrDefault(event.broker().id(), 0);
      if (count == 0) {
        connections.connect(event.broker());
      }
      activated.put(event.broker().id(), ++count);
    }
  }

  @EventListener
  public void on(BrokerDeactivatedEvent event) {
    synchronized (activated) {
      int count = activated.getOrDefault(event.broker().id(), 0);
      if (count == 0) {
        return;
      } else if (count - 1 == 0) {
        connections.disconnect(event.broker());
        activated.remove(event.broker().id());
      } else {
        activated.put(event.broker().id(), --count);
      }
    }
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
