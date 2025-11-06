package clarity.discovery.api;

import clarity.discovery.domain.RabbitMqExchange;
import java.util.Collections;
import java.util.List;

public record ExchangesDto(int count, List<?> elements) {

  public ExchangesDto {
    elements = Collections.unmodifiableList(elements);
  }

  public static ExchangesDto from(List<RabbitMqExchange> exchanges) {
    return new ExchangesDto(exchanges.size(), exchanges.stream().map(ExchangeDto::from).toList());
  }

  public List<?> elements() {
    return Collections.unmodifiableList(this.elements);
  }
}
