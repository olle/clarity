package clarity.connectivity;

import static org.mockito.Mockito.verify;

import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import clarity.management.events.BrokerActivatedEvent;
import clarity.management.events.BrokerDeactivatedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ManageConnectionsOnActivationUseCaseTest {

  @Test
  void connectsOnActivation() {

    var connections = Mockito.mock(Connections.class);
    var sut = new ManageConnectionsOnActivationUseCase(connections, null);

    var broker = new RabbitMqBroker(UUID.randomUUID(), BrokerType.CONFIGURED);
    var event = new BrokerActivatedEvent(broker, null, null);
    sut.on(event);

    verify(connections).connect(broker);
  }

  public void disconnectsOnDeactivation() {
    var connections = Mockito.mock(Connections.class);
    var sut = new ManageConnectionsOnActivationUseCase(connections, null);

    var broker = new RabbitMqBroker(UUID.randomUUID(), BrokerType.CONFIGURED);
    var event = new BrokerDeactivatedEvent(broker, null, null);
    sut.on(event);

    verify(connections).disconnect(broker);
  }
}
