package clarity.brokers;

import java.util.UUID;
import java.util.function.UnaryOperator;

public class Broker {

  public final UUID id;

  private BrokerProperties properties = BrokerProperties.empty();

  public Broker(UUID id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Broker [id=" + id + "]";
  }

  public BrokerProperties properties() {
    return this.properties;
  }

  public Broker withProperties(UnaryOperator<BrokerProperties> mutator) {
    this.properties = mutator.apply(this.properties);
    return this;
  }

  public String getBeanName() {
    return "amqp://%s@%s:%d".formatted(properties.username(), properties.host(), properties.port());
  }
}
