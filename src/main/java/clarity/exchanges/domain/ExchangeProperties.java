package clarity.exchanges.domain;

public record ExchangeProperties(
    String type, Boolean autoDelete, Boolean durable, Boolean internal) {

  public static ExchangeProperties empty() {
    return new ExchangeProperties(null, null, null, null);
  }

  public ExchangeProperties withType(String type) {
    return new ExchangeProperties(type, this.autoDelete, this.durable, this.internal);
  }

  public ExchangeProperties withAutoDelete(boolean autoDelete) {
    return new ExchangeProperties(this.type, autoDelete, this.durable, this.internal);
  }

  public ExchangeProperties withDurable(boolean durable) {
    return new ExchangeProperties(this.type, this.autoDelete, durable, this.internal);
  }

  public ExchangeProperties withInternal(boolean internal) {
    return new ExchangeProperties(this.type, this.autoDelete, this.durable, internal);
  }

  protected ExchangeProperties copy() {
    return new ExchangeProperties(this.type, this.autoDelete, this.durable, this.internal);
  }
}
