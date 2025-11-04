package clarity.discovery.api;

import clarity.discovery.domain.RabbitMqExchange;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.UUID;

@JsonInclude(Include.NON_EMPTY)
public record ExchangeDto(UUID id, String name, String type, UUID brokerId) {

  public static ExchangeDto from(RabbitMqExchange exchange) {
    return new ExchangeDto(
        exchange.id(), exchange.name(), exchange.properties().type(), exchange.brokerId());
  }
}
