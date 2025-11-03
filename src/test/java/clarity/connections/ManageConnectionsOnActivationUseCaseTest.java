package clarity.connections;

import static org.mockito.Mockito.verify;

import clarity.brokers.domain.BrokerType;
import clarity.brokers.domain.RabbitMqBroker;
import clarity.brokers.event.BrokerActivatedEvent;
import clarity.brokers.event.BrokerDeactivatedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ManageConnectionsOnActivationUseCaseTest {

  @Test
  void connectsOnActivation() {

    var connections = Mockito.mock(Connections.class);
    var sut = new ManageConnectionsOnActivationUseCase(connections);

    var broker = new RabbitMqBroker(UUID.randomUUID(), BrokerType.CONFIGURED);
    var event = new BrokerActivatedEvent(broker, null, null);
    sut.on(event);

    verify(connections).connect(broker);
  }

  public void disconnectsOnDeactivation() {
    var connections = Mockito.mock(Connections.class);
    var sut = new ManageConnectionsOnActivationUseCase(connections);

    var broker = new RabbitMqBroker(UUID.randomUUID(), BrokerType.CONFIGURED);
    var event = new BrokerDeactivatedEvent(broker, null, null);
    sut.on(event);

    verify(connections).disconnect(broker);
  }
}
