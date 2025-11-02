package clarity.exchanges;

import clarity.exchanges.domain.RabbitMqExchange;
import java.util.List;

public interface ExchangeRepository {

  void save(RabbitMqExchange exchange);

  List<RabbitMqExchange> findAll();
}
