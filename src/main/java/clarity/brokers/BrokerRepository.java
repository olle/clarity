package clarity.brokers;

import clarity.brokers.api.AccessUseCase;
import clarity.brokers.domain.RabbitMqBroker;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrokerRepository extends AccessUseCase {

  boolean exists(RabbitMqBroker broker);

  void save(RabbitMqBroker broker);

  void deleteById(UUID id);

  List<RabbitMqBroker> findAll();

  List<UUID> findAllRemoved();

  Optional<RabbitMqBroker> findById(UUID id);
}
