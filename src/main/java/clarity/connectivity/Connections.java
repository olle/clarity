package clarity.connectivity;

import clarity.infrastructure.utils.Loggable;
import clarity.infrastructure.utils.Utils;
import clarity.management.domain.RabbitMqBroker;
import java.util.Map;
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
  private final Map<String, RabbitTemplate> templates = new ConcurrentHashMap<>();

  public Connections(GenericApplicationContext context, ApplicationEventPublisher publisher) {
    this.context = context;
    this.publisher = publisher;
  }

  public void connect(RabbitMqBroker broker) {

    var connectionFactory = createConnectionFactory(broker);
    registerConnectionFactory(broker, connectionFactory);

    String rabbitTemplateName = createRabbitTemplateName(broker);
    var rabbitTemplate = createRabbitTemplate(rabbitTemplateName, connectionFactory);
    registerRabbitTemplate(rabbitTemplateName, rabbitTemplate);

    sendOnePingOnly(rabbitTemplate);
  }

  private RabbitMqConnectionFactory createConnectionFactory(RabbitMqBroker broker) {
    RabbitMqConnectionFactory connectionFactory =
        factories.computeIfAbsent(broker.id(), _ -> rabbitMqConnectionFactory(broker));
    logger().info("Created {} from {}", connectionFactory, broker);
    return connectionFactory;
  }

  public RabbitMqConnectionFactory rabbitMqConnectionFactory(RabbitMqBroker broker) {
    return new RabbitMqConnectionFactory(broker, publisher);
  }

  private void registerConnectionFactory(
      RabbitMqBroker broker, RabbitMqConnectionFactory connectionFactory) {
    context.registerBean(
        createConnectionFactoryName(broker),
        RabbitMqConnectionFactory.class,
        () -> connectionFactory);
  }

  private String createConnectionFactoryName(RabbitMqBroker broker) {
    return "connectionFactory<%s>".formatted(broker.name());
  }

  private String createRabbitTemplateName(RabbitMqBroker broker) {
    return "rabbitTemplate<%s>".formatted(broker.name());
  }

  private RabbitTemplate createRabbitTemplate(
      String rabbitTemplateName, RabbitMqConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate =
        templates.computeIfAbsent(rabbitTemplateName, _ -> new RabbitTemplate(connectionFactory));
    logger().info("Created {} from {}", rabbitTemplate, connectionFactory);
    return rabbitTemplate;
  }

  private void registerRabbitTemplate(String rabbitTemplateName, RabbitTemplate rabbitTemplate) {
    context.registerBean(rabbitTemplateName, RabbitTemplate.class, () -> rabbitTemplate);
  }

  public void disconnect(RabbitMqBroker broker) {

    RabbitMqConnectionFactory removed = factories.remove(broker.id());
    logger().info("Disconnected {} for {}", removed, broker);

    if (removed != null) {
      String connectionFactoryName = createConnectionFactoryName(broker);
      String rabbitTemplateName = createRabbitTemplateName(broker);
      context.removeBeanDefinition(connectionFactoryName);
      templates.remove(rabbitTemplateName);
      context.removeBeanDefinition(rabbitTemplateName);
      removed.destroy();
    }
  }

  @Scheduled(fixedDelayString = "PT20S")
  public void scheduled() {
    logger().info("HOLDING {} AND {}", factories, templates);
    for (String rabbitTemplateName : templates.keySet()) {
      sendOnePingOnly(context.getBean(rabbitTemplateName, RabbitTemplate.class));
    }
  }

  private void sendOnePingOnly(RabbitTemplate template) {
    template.send(MessageBuilder.withBody(Utils.toBytes("PING")).build());
  }
}
