package clarity.connections;

import static org.mockito.Mockito.verify;

import clarity.brokers.domain.BrokerType;
import clarity.brokers.domain.RabbitMqBroker;
import clarity.brokers.event.BrokerActivatedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EstablishConnectionOnActivationUseCaseTest {

  @Test
  void connectsOnActivation() {

    var connections = Mockito.mock(Connections.class);
    var sut = new EstablishConnectionOnActivationUseCase(connections);

    var broker = new RabbitMqBroker(UUID.randomUUID(), BrokerType.CONFIGURED);
    var event = new BrokerActivatedEvent(broker, null, null);
    sut.on(event);

    verify(connections).connect(broker);
  }
}
