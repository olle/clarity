package clarity.exchanges.api;

import clarity.exchanges.RabbitMqExchange;
import java.util.UUID;

public record ExchangeDto(UUID id, String name) {

  public static ExchangeDto from(RabbitMqExchange exchange) {
    return new ExchangeDto(exchange.id(), exchange.name());
  }
}
