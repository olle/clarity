package clarity.brokers;

import clarity.brokers.domain.RabbitMqBroker;
import java.util.List;
import java.util.UUID;

public interface BrokerRepository {

  boolean exists(RabbitMqBroker broker);

  void save(RabbitMqBroker broker);

  void deleteById(UUID id);

  List<RabbitMqBroker> findAll();

  List<UUID> findAllRemoved();
}
