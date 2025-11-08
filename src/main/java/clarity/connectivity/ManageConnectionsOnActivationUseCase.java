package clarity.connectivity;

import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import clarity.management.BrokerRepository;
import clarity.management.events.BrokerActivatedEvent;
import clarity.management.events.BrokerDeactivatedEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ManageConnectionsOnActivationUseCase implements UseCase, Loggable {

  private final Connections connections;

  public Map<UUID, Integer> activated = new ConcurrentHashMap<>();

  public ManageConnectionsOnActivationUseCase(
      Connections connections, BrokerRepository brokerRepository) {
    this.connections = connections;
  }

  @EventListener
  public void on(BrokerActivatedEvent event) {
    activated.compute(
        event.broker().id(),
        (_, val) -> {
          if (val == null) {
            connections.connect(event.broker());
            return 1;
          }
          return ++val;
        });
  }

  @EventListener
  public void on(BrokerDeactivatedEvent event) {
    activated.compute(
        event.broker().id(),
        (_, val) -> {
          if (val == null) {
            return null;
          } else if (val - 1 == 0) {
            connections.disconnect(event.broker());
            return null;
          }
          return --val;
        });
  }
}
