package clarity.brokers.api;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.UseCase;
import java.util.UUID;

public interface ManageBrokers extends UseCase {

  void create(RabbitMqBroker rabbitMqBroker);

  void update(RabbitMqBroker rabbitMqBroker);

  void delete(UUID id);

  void activate(UUID id);

  void deactivate(UUID id);
}
