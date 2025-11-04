package clarity.discovery;

import clarity.discovery.domain.RabbitMqExchange;
import java.util.List;

public interface ExchangeRepository {

  void save(RabbitMqExchange exchange);

  List<RabbitMqExchange> findAll();
}
