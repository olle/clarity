package clarity.exchanges;

import java.util.UUID;

public class RabbitMqExchange implements Exchange {

  private final UUID id;
  private final String name;

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
}
