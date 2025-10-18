package clarity.rabbits;

import clarity.rabbits.Brokers.BrokerDto;
import clarity.utils.Loggable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Repository
class BrokerRepository implements Loggable {

  private final ApplicationEventPublisher applicationEventPublisher;

  private final Map<UUID, BrokerEntity> entities = new ConcurrentHashMap<>();

  public BrokerRepository(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  public List<Broker> findAll() {
    return entities.values().stream().map(BrokerEntity::toModel).toList();
  }

  public void save(BrokerDto dto) {

    var entity = BrokerEntity.from(dto);
    var curr = new BrokerEntity(entity);
    var prev = entities.replace(curr.id(), curr);

    if (prev == null) {
      entities.put(curr.id, curr);
      applicationEventPublisher.publishEvent(BrokerAddedEvent.from(curr));
    } else {
      applicationEventPublisher.publishEvent(BrokerUpdatedEvent.from(curr, prev));
    }
  }

  public void deleteById(UUID id) {
    var removed = entities.remove(id);
    if (removed != null) {
      applicationEventPublisher.publishEvent(BrokerRemovedEvent.from(removed));
    }
  }

  record BrokerEntity(
      UUID id, String host, int port, String username, String password, boolean ssl) {

    public BrokerEntity(BrokerEntity other) {
      this(other.id, other.host, other.port, other.username, other.password, other.ssl);
    }

    public static BrokerEntity from(BrokerDto dto) {
      return new BrokerEntity(
          createId(dto), dto.host(), dto.port(), dto.username(), dto.password(), dto.ssl());
    }

    static UUID createId(BrokerDto dto) {
      return UUID.nameUUIDFromBytes(
          "%s@%s:%d".formatted(dto.username(), dto.host(), dto.port()).getBytes());
    }

    public Broker toModel() {
      return new Broker(this.id);
    }
  }
}
