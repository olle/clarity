package clarity.connections;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import clarity.brokers.Broker;
import clarity.brokers.BrokerAddedEvent;
import clarity.infrastructure.utils.Loggable;

@Component
public class Connections implements Loggable {

	private final ApplicationContext context;

	public Connections(ApplicationContext context) {
		this.context = context;
	}

	public void on(BrokerAddedEvent event) {
		RabbitMqConnectionFactory connectionFactory = (RabbitMqConnectionFactory) context
				.getBean(RabbitMqConnectionFactory.class.getSimpleName(), event.broker());
		Connection connection = connectionFactory.createConnection();
		logger().info("Created {} from {}", connection, connectionFactory);
	}

	@Bean
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public RabbitMqConnectionFactory rabbitMqConnectionFactory(Broker broker) {
		return new RabbitMqConnectionFactory(broker);
	}

}
