package clarity.connections;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import clarity.brokers.Broker;


public class RabbitMqConnectionFactory extends CachingConnectionFactory {
	
	public RabbitMqConnectionFactory(Broker broker) {
		super(broker.properties().host(), broker.properties().port());
		setUsername(broker.properties().username());
		setPassword(broker.properties().password());
	}
}
