package clarity.brokers.api;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.UseCase;
import java.util.UUID;

public interface ManageUseCase extends UseCase {

  void create(RabbitMqBroker rabbitMqBroker);

  void update(RabbitMqBroker rabbitMqBroker);

  void delete(UUID id);
}
