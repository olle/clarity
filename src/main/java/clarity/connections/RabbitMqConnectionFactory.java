package clarity.connections;

import clarity.brokers.RabbitMqBroker;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

public class RabbitMqConnectionFactory extends CachingConnectionFactory {

  public RabbitMqConnectionFactory(RabbitMqBroker broker) {
    super(broker.properties().host(), broker.properties().port());
    setUsername(broker.properties().username());
    setPassword(broker.properties().password());
  }
}
