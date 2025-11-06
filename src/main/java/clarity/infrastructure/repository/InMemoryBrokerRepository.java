package clarity.infrastructure.repository;

import clarity.infrastructure.utils.Loggable;
import clarity.management.BrokerRepository;
import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import clarity.management.events.BrokerAddedEvent;
import clarity.management.events.BrokerMarkedRemovedEvent;
import clarity.management.events.BrokerRemovedEvent;
import clarity.management.events.BrokerUpdatedEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationEventPublisher;

public class InMemoryBrokerRepository implements Loggable, BrokerRepository {

  private final ApplicationEventPublisher applicationEventPublisher;

  private final Map<UUID, BrokerEntity> entities = new ConcurrentHashMap<>();
  private final Set<UUID> deleted = ConcurrentHashMap.newKeySet();

  public InMemoryBrokerRepository(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public Optional<RabbitMqBroker> findById(UUID id) {
    return Optional.ofNullable(entities.get(id)).map(BrokerEntity::toModel);
  }

  @Override
  public List<RabbitMqBroker> findAll() {
    return Collections.unmodifiableList(
        entities.values().stream().map(BrokerEntity::toModel).toList());
  }

  @Override
  public List<UUID> findAllRemoved() {
    return Collections.unmodifiableList(deleted.stream().toList());
  }

  @Override
  public boolean exists(RabbitMqBroker broker) {

    if (broker.id() == null) {
      return entities.containsKey(BrokerEntity.from(broker).id());
    } else {
      return entities.containsKey(broker.id());
    }
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
    deleted.remove(entity.id());

    if (prev == null) {
      entities.put(entity.id(), curr);
      applicationEventPublisher.publishEvent(BrokerAddedEvent.from(curr.toModel()));
    } else {
      applicationEventPublisher.publishEvent(
          BrokerUpdatedEvent.from(curr.toModel(), prev.toModel()));
    }
  }

  @Override
  public synchronized void deleteById(UUID id) {

    var removed = entities.remove(id);
    boolean markedRemoved = deleted.add(id);

    if (removed == null) {

      if (markedRemoved) {
        applicationEventPublisher.publishEvent(BrokerMarkedRemovedEvent.from(id));
      }

      return;
    }

    if (!BrokerType.MANAGED.name().equals(removed.type())) {
      entities.put(id, removed);
      deleted.remove(id);
      throw new IllegalArgumentException("Cannot delete a configured broker");
    }

    applicationEventPublisher.publishEvent(BrokerRemovedEvent.from(removed.toModel()));
  }
}
