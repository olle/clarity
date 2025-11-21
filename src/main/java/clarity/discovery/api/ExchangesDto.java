package clarity.discovery.api;

import clarity.discovery.domain.RabbitMqExchange;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;

@SuppressFBWarnings("EI_EXPOSE_REP")
public record ExchangesDto(int count, List<ExchangeDto> elements) {

  public static ExchangesDto from(List<RabbitMqExchange> exchanges) {
    return new ExchangesDto(exchanges.size(), exchanges.stream().map(ExchangeDto::from).toList());
  }
}
