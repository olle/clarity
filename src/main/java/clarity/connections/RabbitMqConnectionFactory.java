package clarity.connections;

import clarity.brokers.Broker;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

public class RabbitMqConnectionFactory extends CachingConnectionFactory {

  public RabbitMqConnectionFactory(Broker broker) {
    super(broker.properties().host(), broker.properties().port());
    setUsername(broker.properties().username());
    setPassword(broker.properties().password());
  }
}
