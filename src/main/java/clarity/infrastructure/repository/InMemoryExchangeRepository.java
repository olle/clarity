package clarity.infrastructure.repository;

import clarity.exchanges.ExchangeRepository;
import clarity.exchanges.domain.RabbitMqExchange;
import clarity.exchanges.event.ExchangeAddedEvent;
import clarity.exchanges.event.ExchangeUpdatedEvent;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationEventPublisher;

public class InMemoryExchangeRepository implements ExchangeRepository {

  private final ApplicationEventPublisher publisher;
  private final Map<UUID, ExchangeEntity> entities = new ConcurrentHashMap<>();

  public InMemoryExchangeRepository(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void save(RabbitMqExchange exchange) {
    if (exchange.id() == null) {
      save(ExchangeEntity.from(exchange));
    } else {
      save(ExchangeEntity.from(exchange).withId(exchange.id()));
    }
  }

  private void save(ExchangeEntity entity) {

    var curr = new ExchangeEntity(entity);
    var prev = entities.replace(curr.id(), curr);

    if (prev == null) {
      entities.put(curr.id(), curr);
      publisher.publishEvent(ExchangeAddedEvent.from(curr.toModel()));
    } else {
      publisher.publishEvent(ExchangeUpdatedEvent.from(curr.toModel(), prev.toModel()));
    }
  }

  @Override
  public List<RabbitMqExchange> findAll() {
    return entities.values().stream().map(ExchangeEntity::toModel).toList();
  }
}
