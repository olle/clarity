package clarity.management.domain;

import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class RabbitMqBroker {

  private final UUID id;
  private final BrokerType type;

  private String host;
  private Integer port;
  private String username;
  private String password;

  private BrokerProperties properties = BrokerProperties.empty();
  private BrokerAttributes attributes = new BrokerAttributes();

  public RabbitMqBroker(UUID id, BrokerType type) {
    this.id = id;
    this.type = type;
  }

  public RabbitMqBroker with(String host, Integer port, String username) {
    this.host = host;
    this.port = port;
    this.username = username;
    return this;
  }

  public RabbitMqBroker with(String host, Integer port, String username, String password) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
    return this;
  }

  public UUID id() {
    return this.id;
  }

  public BrokerType type() {
    return this.type;
  }

  public String name() {
    return "amqp://%s@%s:%d".formatted(username, host, port);
  }

  public String host() {
    return host;
  }

  public Integer port() {
    return port;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
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

  public BrokerAttributes attributes() {
    return this.attributes;
  }

  public RabbitMqBroker withAttributes(UnaryOperator<BrokerAttributes> decorator) {
    this.attributes = decorator.apply(this.attributes);
    return this;
  }

  public RabbitMqBroker withAttributes(Map<String, Object> attributes) {
    this.attributes.putAll(attributes);
    return this;
  }

  public void activate() {
    withProperties(props -> props.withActive(true));
  }

  public void deactivate() {
    withProperties(props -> props.withActive(false));
  }

  public void connected() {
    withProperties(props -> props.withConnected(true));
  }

  public void disconnected() {
    withProperties(props -> props.withConnected(false));
  }
}
