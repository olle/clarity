package clarity.brokers.api;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.Adapter;
import java.util.List;
import java.util.UUID;

public interface AccessBrokers extends Adapter {

  List<RabbitMqBroker> findAll();

  List<UUID> findAllRemoved();
}
