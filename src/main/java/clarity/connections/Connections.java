package clarity.connections;

import clarity.brokers.Broker;
import clarity.brokers.BrokerAddedEvent;
import clarity.infrastructure.utils.Loggable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Connections implements Loggable {

  private final GenericApplicationContext context;
  private final Map<UUID, RabbitMqConnectionFactory> factories = new ConcurrentHashMap<>();

  public Connections(GenericApplicationContext context) {
    this.context = context;
  }

  @EventListener
  public void on(BrokerAddedEvent event) {

    Broker broker = event.broker();
	RabbitMqConnectionFactory rabbitMqConnectionFactory = rabbitMqConnectionFactory(broker);
    logger().info("Created {} from {}", rabbitMqConnectionFactory, event);
    factories.putIfAbsent(broker.id, rabbitMqConnectionFactory);
    
    context.registerBean(
        broker.getBeanName(),
        RabbitMqConnectionFactory.class,
        () -> rabbitMqConnectionFactory,
        def -> def.setScope(BeanDefinition.SCOPE_PROTOTYPE));
  }

  public RabbitMqConnectionFactory rabbitMqConnectionFactory(Broker broker) {
    return new RabbitMqConnectionFactory(broker);
  }

  @Scheduled(fixedDelayString = "PT10S")
  public void scheduled() {
    logger().info("HOLDING {}", factories);
    Map<String, RabbitMqConnectionFactory> beans =
        context.getBeansOfType(RabbitMqConnectionFactory.class);
    logger().info("BEANS {}", beans);
  }
}
