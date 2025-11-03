package clarity.brokers.api;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.UseCase;
import java.util.List;
import java.util.UUID;

public interface AccessBrokers extends UseCase {

  List<RabbitMqBroker> findAll();

  List<UUID> findAllRemoved();
}
