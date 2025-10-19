package clarity.brokers;

import clarity.brokers.BrokerController.BrokerDto;
import clarity.brokers.BrokersConfigurationProperty.ConfiguredBroker;
import clarity.infrastructure.utils.Loggable;
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

  public void save(UUID id, BrokerDto dto) {
    save(BrokerEntity.from(dto).withId(id));
  }

  public void save(BrokerDto brokerDto) {
    save(BrokerEntity.from(brokerDto));
  }

  public void save(ConfiguredBroker configuredBroker) {
    save(BrokerEntity.from(configuredBroker));
  }

  private void save(BrokerEntity entity) {

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

    public static BrokerEntity from(BrokerDto brokerDto) {
      return new BrokerEntity(
          createId(brokerDto),
          brokerDto.host(),
          brokerDto.port(),
          brokerDto.username(),
          brokerDto.password(),
          brokerDto.ssl());
    }

    public static BrokerEntity from(ConfiguredBroker configuredBroker) {
      return new BrokerEntity(
          createId(configuredBroker),
          configuredBroker.getHost(),
          configuredBroker.getPort(),
          configuredBroker.getUsername(),
          configuredBroker.getPassword(),
          configuredBroker.isSsl());
    }

    public BrokerEntity withId(UUID id) {
      return new BrokerEntity(id, this.host, this.port, this.username, this.password, this.ssl);
    }

    static UUID createId(BrokerDto dto) {
      return createUuidFrom(dto.username(), dto.host(), dto.port());
    }

    static UUID createId(ConfiguredBroker configuredBroker) {
      return createUuidFrom(
          configuredBroker.getUsername(), configuredBroker.getHost(), configuredBroker.getPort());
    }

    private static UUID createUuidFrom(String username, String host, Integer port) {
      return UUID.nameUUIDFromBytes("%s@%s:%d".formatted(username, host, port).getBytes());
    }

    public Broker toModel() {
      return new Broker(this.id)
          .withProperties(
              properties ->
                  properties
                      .withHost(host)
                      .withPort(port)
                      .withUsername(username)
                      .withPassword(password)
                      .withSSL(ssl));
    }
  }
}
