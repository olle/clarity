package clarity.management.api;

import clarity.infrastructure.Adapter;
import clarity.management.domain.RabbitMqBroker;
import java.util.List;
import java.util.UUID;

public interface AccessBrokers extends Adapter {

  List<RabbitMqBroker> findAll();

  List<UUID> findAllRemoved();
}
