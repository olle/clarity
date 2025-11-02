package clarity.exchanges;

import java.util.UUID;
import java.util.function.UnaryOperator;

public class RabbitMqExchange implements Exchange {

  private final UUID id;
  private final String name;
  private UUID brokerId;
  private ExchangeProperties properties = ExchangeProperties.empty();

  public RabbitMqExchange(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public String toString() {
    return "RabbitMqExchange [id=" + id + ", name=" + name + "]";
  }

  @Override
  public UUID id() {
    return this.id;
  }

  @Override
  public String name() {
    return this.name;
  }

  public UUID brokerId() {
    return brokerId;
  }

  public RabbitMqExchange withBrokerId(UUID brokerId) {
    this.brokerId = brokerId;
    return this;
  }

  public ExchangeProperties properties() {
    return this.properties;
  }

  public RabbitMqExchange withProperties(UnaryOperator<ExchangeProperties> decorator) {
    this.properties = decorator.apply(this.properties);
    return this;
  }
}
