package clarity.connections;

import clarity.brokers.Broker;
import clarity.brokers.BrokerAddedEvent;
import clarity.infrastructure.utils.Loggable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Connections implements Loggable {

  private final Map<UUID, RabbitMqConnectionFactory> factories = new ConcurrentHashMap<>();

  @EventListener
  public void on(BrokerAddedEvent event) {

    RabbitMqConnectionFactory rabbitMqConnectionFactory = rabbitMqConnectionFactory(event.broker());
    logger().info("Created {} from {}", rabbitMqConnectionFactory, event);
    factories.putIfAbsent(event.broker().id, rabbitMqConnectionFactory);
  }

  public RabbitMqConnectionFactory rabbitMqConnectionFactory(Broker broker) {
    return new RabbitMqConnectionFactory(broker);
  }

  @Scheduled(fixedDelayString = "PT10S")
  public void scheduled() {
    logger().info("HOLDING {}", factories);

    if (!factories.isEmpty()) {
      RabbitMqConnectionFactory factory =
          factories.values().stream()
              .toList()
              .get(ThreadLocalRandom.current().nextInt(factories.size()));
      Connection connection = factory.createConnection();
      logger().info("CREATED CONNECTION: {}", connection);

      try {
        Thread.sleep(12345);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
