package clarity.connections;

import clarity.brokers.domain.RabbitMqBroker;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.util.ObjectUtils;

public class RabbitMqConnectionFactory extends CachingConnectionFactory {

  private final AtomicInteger connectionCounter = new AtomicInteger();

  public RabbitMqConnectionFactory(RabbitMqBroker broker) {
    super(broker.properties().host(), broker.properties().port());
    setConnectionNameStrategy(
        _ ->
            "Clarity#%s:%d"
                .formatted(
                    ObjectUtils.getIdentityHexString(this), connectionCounter.incrementAndGet()));
    setUsername(broker.properties().username());
    setPassword(broker.properties().password());
  }
}
