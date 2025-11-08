package clarity.connectivity;

import clarity.infrastructure.utils.Loggable;
import clarity.infrastructure.utils.Utils;
import clarity.management.domain.RabbitMqBroker;
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

    var connectionFactory = createConnectionFactory(broker);
    registerConnectionFactory(broker, connectionFactory);

    var rabbitTemplate = createRabbitTemplate(connectionFactory);
    registerRabbitTemplate(broker, rabbitTemplate);

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

  private RabbitTemplate createRabbitTemplate(RabbitMqConnectionFactory connectionFactory) {
    return new RabbitTemplate(connectionFactory);
  }

  private void registerRabbitTemplate(RabbitMqBroker broker, RabbitTemplate rabbitTemplate) {
    String rabbitTemplateName = createRabbitTemplateName(broker);
    templateNames.add(rabbitTemplateName);
    context.registerBean(rabbitTemplateName, RabbitTemplate.class, () -> rabbitTemplate);
  }

  private String createRabbitTemplateName(RabbitMqBroker broker) {
    return "rabbitTemplate<%s>".formatted(broker.name());
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

  @Scheduled(fixedDelayString = "PT20S")
  public void scheduled() {
    logger().info("HOLDING {}", factories);
    for (String rabbitTemplateName : templateNames) {
      sendOnePingOnly(context.getBean(rabbitTemplateName, RabbitTemplate.class));
    }
  }

  private void sendOnePingOnly(RabbitTemplate template) {
    template.send(MessageBuilder.withBody(Utils.toBytes("PING")).build());
  }
}
