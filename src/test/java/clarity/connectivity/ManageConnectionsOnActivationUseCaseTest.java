package clarity.connectivity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
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

  @Test
  void activationsAreRetainedAndCounted() {

    var connections = Mockito.mock(Connections.class);
    var sut = new ManageConnectionsOnActivationUseCase(connections, null);

    assertThat(sut.activated).isEmpty();

    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    UUID id3 = UUID.randomUUID();

    var e1 = brokerActivatedFixture(id1);
    var e2 = brokerActivatedFixture(id2);
    var e3 = brokerActivatedFixture(id3);

    sut.on(e1);
    sut.on(e1);

    assertThat(sut.activated).hasSize(1).contains(entry(id1, 2));
    verify(connections).connect(e1.broker());

    sut.on(e2);
    sut.on(e2);
    sut.on(e2);

    assertThat(sut.activated).hasSize(2).contains(entry(id1, 2), entry(id2, 3));
    verify(connections).connect(e2.broker());

    sut.on(e3);
    sut.on(e3);
    sut.on(e3);
    sut.on(e3);

    assertThat(sut.activated).hasSize(3).contains(entry(id1, 2), entry(id2, 3), entry(id3, 4));
    verify(connections).connect(e3.broker());
  }

  @Test
  void deactivationsAreLatches() {

    var connections = Mockito.mock(Connections.class);
    var sut = new ManageConnectionsOnActivationUseCase(connections, null);

    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    UUID id3 = UUID.randomUUID();

    assertThat(sut.activated).isEmpty();

    sut.on(brokerActivatedFixture(id1));
    sut.on(brokerActivatedFixture(id1));
    sut.on(brokerActivatedFixture(id2));
    sut.on(brokerActivatedFixture(id3));
    sut.on(brokerActivatedFixture(id3));
    sut.on(brokerActivatedFixture(id3));
    sut.on(brokerActivatedFixture(id3));

    assertThat(sut.activated).hasSize(3).contains(entry(id1, 2), entry(id2, 1), entry(id3, 4));

    var e1 = brokerDeactivatedFixture(id1);
    var e2 = brokerDeactivatedFixture(id2);
    var e3 = brokerDeactivatedFixture(id3);

    sut.on(e1);
    sut.on(e1);
    sut.on(e2);
    sut.on(e2);
    sut.on(e2);

    assertThat(sut.activated).hasSize(1).contains(entry(id3, 4));

    verify(connections).disconnect(e1.broker());
    verify(connections).disconnect(e2.broker());

    assertThat(sut.activated).hasSize(1);

    sut.on(e3);
    assertThat(sut.activated).hasSize(1).contains(entry(id3, 3));

    sut.on(e3);
    sut.on(e3);
    sut.on(e3);

    assertThat(sut.activated).isEmpty();
    verify(connections).disconnect(e3.broker());
  }

  private BrokerDeactivatedEvent brokerDeactivatedFixture(UUID id1) {
    var broker = new RabbitMqBroker(id1, BrokerType.CONFIGURED);
    var event = new BrokerDeactivatedEvent(broker, null, null);
    return event;
  }

  private BrokerActivatedEvent brokerActivatedFixture(UUID id1) {
    return new BrokerActivatedEvent(new RabbitMqBroker(id1, BrokerType.MANAGED), null, null);
  }
}
