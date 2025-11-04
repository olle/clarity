package clarity.connections;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.connections.events.BrokerConnectedEvent;
import clarity.connections.events.BrokerDisconnectedEvent;
import clarity.infrastructure.utils.Loggable;
import com.rabbitmq.client.ShutdownSignalException;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.ObjectUtils;

public class RabbitMqConnectionFactory extends CachingConnectionFactory implements Loggable {

  private final RabbitMqBroker broker;
  private final ApplicationEventPublisher publisher;

  private final AtomicInteger connectionCounter = new AtomicInteger();

  public RabbitMqConnectionFactory(RabbitMqBroker broker, ApplicationEventPublisher publisher) {

    super(broker.properties().host(), broker.properties().port());

    this.broker = broker;
    this.publisher = publisher;

    setConnectionNameStrategy(createNamingStrategy());
    setUsername(broker.properties().username());
    setPassword(broker.properties().password());
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
          notifyConnected();
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

      private void notifyConnected() {
        publisher.publishEvent(BrokerConnectedEvent.from(broker));
      }

      private void notifyDisconnected() {
        publisher.publishEvent(BrokerDisconnectedEvent.from(broker));
      }
    };
  }
}
