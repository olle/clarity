package clarity.brokers;

import java.util.UUID;
import java.util.function.UnaryOperator;

public class RabbitMqBroker implements Broker {

  private final UUID id;
  private final BrokerType type;

  private boolean active;

  private BrokerProperties properties = BrokerProperties.empty();

  public RabbitMqBroker(UUID id, BrokerType type) {
    this.id = id;
    this.type = type;
    this.active = false;
  }

  @Override
  public UUID id() {
    return this.id;
  }

  @Override
  public BrokerType type() {
    return this.type;
  }

  @Override
  public boolean active() {
    return this.active;
  }

  @Override
  public String toString() {
    return "RabbitMqBroker [id=" + id + ", type=" + type + "]";
  }

  public BrokerProperties properties() {
    return this.properties;
  }

  public RabbitMqBroker withProperties(UnaryOperator<BrokerProperties> mapper) {
    this.properties = mapper.apply(this.properties);
    return this;
  }

  public String getBeanName() {
    return "amqp://%s@%s:%d".formatted(properties.username(), properties.host(), properties.port());
  }
}
