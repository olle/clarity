package clarity.connections;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.utils.Loggable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class Connections implements Loggable {

  private final GenericApplicationContext context;
  private final ApplicationEventPublisher publisher;

  private final Map<UUID, RabbitMqConnectionFactory> factories = new ConcurrentHashMap<>();
  private final Set<String> templateNames = new HashSet<>();

  public Connections(GenericApplicationContext context, ApplicationEventPublisher publisher) {
    this.context = context;
    this.publisher = publisher;
  }

  public void connect(RabbitMqBroker broker) {

    RabbitMqConnectionFactory connectionFactory = rabbitMqConnectionFactory(broker);

    logger().info("Created {} from {}", connectionFactory, broker);
    factories.putIfAbsent(broker.id(), connectionFactory);

    context.registerBean(
        createConnectionFactoryName(broker),
        RabbitMqConnectionFactory.class,
        () -> connectionFactory);

    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    String rabbitTemplateName = createRabbitTemplateName(broker);
    templateNames.add(rabbitTemplateName);
    context.registerBean(rabbitTemplateName, RabbitTemplate.class, () -> rabbitTemplate);

    sendHelloWorld(rabbitTemplate);
  }

  public void disconnect(RabbitMqBroker broker) {

    RabbitMqConnectionFactory removed = factories.remove(broker.id());
    logger().info("Disconnected {} for {}", removed, broker);

    if (removed != null) {
      context.removeBeanDefinition(createConnectionFactoryName(broker));
      String rabbitTemplateName = createRabbitTemplateName(broker);
      templateNames.remove(rabbitTemplateName);
      context.removeBeanDefinition(rabbitTemplateName);
      removed.destroy();
    }
  }

  private String createRabbitTemplateName(RabbitMqBroker broker) {
    return "rabbitTemplate<%s>".formatted(broker.name());
  }

  private String createConnectionFactoryName(RabbitMqBroker broker) {
    return "connectionFactory<%s>".formatted(broker.name());
  }

  public RabbitMqConnectionFactory rabbitMqConnectionFactory(RabbitMqBroker broker) {
    return new RabbitMqConnectionFactory(broker, publisher);
  }

  @Scheduled(fixedDelayString = "PT20S")
  public void scheduled() {

    logger().info("HOLDING {}", factories);
    for (String rabbitTemplateName : templateNames) {
      sendHelloWorld(context.getBean(rabbitTemplateName, RabbitTemplate.class));
    }
  }

  private void sendHelloWorld(RabbitTemplate template) {
    template.send(MessageBuilder.withBody("Hello world!".getBytes()).build());
  }
}
