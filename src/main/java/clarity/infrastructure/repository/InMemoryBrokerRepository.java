package clarity.infrastructure.repository;

import clarity.brokers.Broker;
import clarity.brokers.BrokerRepository;
import clarity.brokers.BrokerType;
import clarity.brokers.RabbitMqBroker;
import clarity.brokers.event.BrokerAddedEvent;
import clarity.brokers.event.BrokerRemovedEvent;
import clarity.brokers.event.BrokerUpdatedEvent;
import clarity.infrastructure.utils.Loggable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationEventPublisher;

public class InMemoryBrokerRepository implements Loggable, BrokerRepository {

  private final ApplicationEventPublisher applicationEventPublisher;

  private final Map<UUID, BrokerEntity> entities = new ConcurrentHashMap<>();

  public InMemoryBrokerRepository(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public List<RabbitMqBroker> findAll() {
    return entities.values().stream().map(BrokerEntity::toModel).toList();
  }

  @Override
  public void save(RabbitMqBroker rabbitMqBroker) {

    if (rabbitMqBroker.id() == null) {
      save(BrokerEntity.from(rabbitMqBroker));
    } else {
      save(BrokerEntity.from(rabbitMqBroker).withId(rabbitMqBroker.id()));
    }
  }

  private void save(BrokerEntity entity) {

    var curr = new BrokerEntity(entity);
    var prev = entities.replace(curr.id(), curr);

    if (prev == null) {
      entities.put(curr.id(), curr);
      applicationEventPublisher.publishEvent(BrokerAddedEvent.from(curr.toModel()));
    } else {
      applicationEventPublisher.publishEvent(
          BrokerUpdatedEvent.from(curr.toModel(), prev.toModel()));
    }
  }

  @Override
  public void deleteById(UUID id) {

    synchronized (entities) {
      var removed = entities.remove(id);

      if (removed == null) {
        return;
      }

      if (!BrokerType.MANAGED.name().equals(removed.type())) {
        entities.put(id, removed);
        throw new IllegalArgumentException("Cannot delete a configured broker");
      }

      applicationEventPublisher.publishEvent(BrokerRemovedEvent.from(removed.toModel()));
    }
  }

  public Optional<Broker> findById(UUID id) {
    return Optional.ofNullable(entities.get(id)).map(BrokerEntity::toModel);
  }
}
