package clarity.exchanges;

import java.util.List;

public interface ExchangeRepository {

  void save(RabbitMqExchange exchange);

  List<RabbitMqExchange> findAll();
}
