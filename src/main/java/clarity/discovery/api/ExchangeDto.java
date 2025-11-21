package clarity.discovery.api;

import clarity.discovery.domain.RabbitMqExchange;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import java.util.UUID;

@SuppressFBWarnings("EI_EXPOSE_REP")
@JsonInclude(Include.NON_EMPTY)
public record ExchangeDto(
    UUID id, String name, String type, UUID brokerId, Map<String, Object> attributes) {

  public static ExchangeDto from(RabbitMqExchange exchange) {
    return new ExchangeDto(
        exchange.id(),
        exchange.name(),
        exchange.properties().type(),
        exchange.brokerId(),
        exchange.attributes());
  }
}
