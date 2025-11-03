package clarity.connections;

import clarity.brokers.event.BrokerActivatedEvent;
import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EstablishConnectionOnActivationUseCase implements UseCase, Loggable {

  private final Connections connections;

  public EstablishConnectionOnActivationUseCase(Connections connections) {
    this.connections = connections;
  }

  @EventListener
  public void on(BrokerActivatedEvent event) {
    connections.connect(event.broker());
  }
}
