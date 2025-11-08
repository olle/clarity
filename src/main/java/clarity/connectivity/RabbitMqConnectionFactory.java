package clarity.connectivity;

import clarity.connectivity.events.BrokerConnectedEvent;
import clarity.connectivity.events.BrokerDisconnectedEvent;
import clarity.infrastructure.utils.Loggable;
import clarity.management.domain.RabbitMqBroker;
import com.rabbitmq.client.ShutdownSignalException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

public class RabbitMqConnectionFactory extends CachingConnectionFactory implements Loggable {

  private final RabbitMqBroker broker;
  private final ApplicationEventPublisher publisher;

  private final AtomicInteger connectionCounter = new AtomicInteger();

  public RabbitMqConnectionFactory(RabbitMqBroker broker, ApplicationEventPublisher publisher) {

    super(broker.host(), broker.port());

    this.broker = broker;
    this.publisher = publisher;

    setConnectionNameStrategy(createNamingStrategy());
    setUsername(broker.username());
    setPassword(broker.password());
    addConnectionListener(createConnectionListener());
  }

  private ConnectionNameStrategy createNamingStrategy() {
    return _ ->
        "Clarity#%s:%d"
            .formatted(ObjectUtils.getIdentityHexString(this), connectionCounter.incrementAndGet());
  }

  private ConnectionListener createConnectionListener() {
    return new ConnectionListener() {

      @Override
      public void onCreate(Connection connection) {
        if (connection.isOpen()) {
          logger().info("Connection opened {}", connection);
          notifyConnected(
              Optional.ofNullable(connection.getDelegate())
                  .map(com.rabbitmq.client.Connection::getClientProvidedName)
                  .orElse(null));
        }
      }

      @Override
      public void onClose(Connection connection) {
        if (!connection.isOpen()) {
          logger().info("Connection closed {}", connection);
          notifyDisconnected();
        }
      }

      @Override
      public void onFailed(Exception exception) {
        notifyDisconnected();
      }

      @Override
      public void onShutDown(ShutdownSignalException signal) {
        notifyDisconnected();
      }

      private void notifyConnected(@Nullable String id) {
        publisher.publishEvent(
            BrokerConnectedEvent.from(
                broker.withAttributes(attr -> attr.withNullable("connection", id))));
      }

      private void notifyDisconnected() {
        publisher.publishEvent(
            BrokerDisconnectedEvent.from(
                broker.withAttributes(attr -> attr.without("connection"))));
      }
    };
  }
}
