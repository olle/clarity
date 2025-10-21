package clarity.connections;

import clarity.brokers.Broker;
import clarity.brokers.BrokerAddedEvent;
import clarity.brokers.BrokerRemovedEvent;
import clarity.infrastructure.utils.Loggable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
  public void on(BrokerRemovedEvent event) {

    Broker broker = event.removed();
    RabbitMqConnectionFactory removed = factories.remove(broker.id);

    if (removed != null) {
      context.removeBeanDefinition(createConnectionFactoryName(broker));
      context.removeBeanDefinition(createRabbitTemplateName(broker));
    }
  }

  @EventListener
  public void on(BrokerAddedEvent event) {

    Broker broker = event.broker();
    RabbitMqConnectionFactory connectionFactory = rabbitMqConnectionFactory(broker);

    logger().info("Created {} from {}", connectionFactory, event);
    factories.putIfAbsent(broker.id, connectionFactory);

    context.registerBean(
        createConnectionFactoryName(broker),
        RabbitMqConnectionFactory.class,
        () -> connectionFactory);

    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    context.registerBean(
        createRabbitTemplateName(broker), RabbitTemplate.class, () -> rabbitTemplate);
  }

  private String createRabbitTemplateName(Broker broker) {
    return "rabbitTemplate" + broker.getBeanName();
  }

  private String createConnectionFactoryName(Broker broker) {
    return "connectionFactory" + broker.getBeanName();
  }

  public RabbitMqConnectionFactory rabbitMqConnectionFactory(Broker broker) {
    return new RabbitMqConnectionFactory(broker);
  }

  @Scheduled(fixedDelayString = "PT10S")
  public void scheduled() {
    logger().info("HOLDING {}", factories);
  }
}
