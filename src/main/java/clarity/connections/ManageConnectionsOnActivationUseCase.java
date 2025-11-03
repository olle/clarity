package clarity.connections;

import clarity.brokers.event.BrokerActivatedEvent;
import clarity.brokers.event.BrokerDeactivatedEvent;
import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ManageConnectionsOnActivationUseCase implements UseCase, Loggable {

  private final Connections connections;

  public ManageConnectionsOnActivationUseCase(Connections connections) {
    this.connections = connections;
  }

  @EventListener
  public void on(BrokerActivatedEvent event) {
    connections.connect(event.broker());
  }

  @EventListener
  public void on(BrokerDeactivatedEvent event) {
    connections.disconnect(event.broker());
  }
}
