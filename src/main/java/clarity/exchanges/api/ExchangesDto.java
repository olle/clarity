package clarity.exchanges.api;

import clarity.exchanges.RabbitMqExchange;
import java.util.List;

public record ExchangesDto(int count, List<?> elements) {

  public static ExchangesDto from(List<RabbitMqExchange> exchanges) {
    return new ExchangesDto(exchanges.size(), exchanges.stream().map(ExchangeDto::from).toList());
  }
}
