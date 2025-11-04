package clarity.discovery;

import clarity.discovery.domain.RabbitMqExchange;
import clarity.discovery.events.ExchangeResolvedEvent;
import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import clarity.management.domain.BrokerProperties;
import clarity.management.domain.RabbitMqBroker;
import clarity.management.events.BrokerAddedEvent;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
class ResolveExchangesFromApiUseCase implements UseCase, Loggable {

  private final RestClient restClient;
  private final ExchangeRepository repo;
  private final ApplicationEventPublisher publisher;

  public ResolveExchangesFromApiUseCase(
      RestClient restClient, ApplicationEventPublisher publisher, ExchangeRepository repo) {
    this.restClient = restClient;
    this.repo = repo;
    this.publisher = publisher;
  }

  @Async
  @EventListener
  public void on(BrokerAddedEvent event) {

    logger().info("Handling {}", event);

    RabbitMqBroker broker = event.broker();

    List<RabbitMqExchange> rabbitMqExchanges =
        fetch(
                broker.properties(),
                "exchanges",
                new ParameterizedTypeReference<Collection<ExchangeDto>>() {})
            .stream()
            .map(dto -> dto.toRabbitMqExchange(broker))
            .toList();

    rabbitMqExchanges.forEach(repo::save);

    rabbitMqExchanges.stream()
        .map(exchange -> ExchangeResolvedEvent.from(exchange, event.broker()))
        .forEach(publisher::publishEvent);
  }

  private <T> T fetch(BrokerProperties props, String endpoint, ParameterizedTypeReference<T> type) {
    return restClient
        .get()
        .uri("http://%s:%d/api/%s".formatted(props.host(), props.httpPort(), endpoint))
        .header("Authorization", encodeBasic(props.username(), props.password()))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(type);
  }

  private String encodeBasic(String username, String password) {
    return "Basic %s"
        .formatted(Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
  }

  record ExchangeDto(
      String name,
      String type,
      boolean auto_delete,
      boolean durable,
      boolean internal,
      Map<String, Object> arguments) {

    public RabbitMqExchange toRabbitMqExchange(RabbitMqBroker rabbitMqBroker) {
      return new RabbitMqExchange(null, name)
          .withBrokerId(rabbitMqBroker.id())
          .withProperties(
              props ->
                  props
                      .withType(type)
                      .withAutoDelete(auto_delete)
                      .withDurable(durable)
                      .withInternal(internal));
    }
  }
}
