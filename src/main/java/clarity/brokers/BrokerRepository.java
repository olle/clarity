package clarity.brokers;

import java.util.List;
import java.util.UUID;

public interface BrokerRepository {

  void save(RabbitMqBroker rabbitMqBroker);

  void deleteById(UUID id);

  List<RabbitMqBroker> findAll();
}
