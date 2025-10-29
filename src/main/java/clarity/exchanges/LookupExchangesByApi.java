package clarity.exchanges;

import clarity.brokers.BrokerProperties;
import clarity.brokers.RabbitMqBroker;
import clarity.brokers.event.BrokerAddedEvent;
import clarity.exchanges.event.ExchangeResolvedEvent;
import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LookupExchangesByApi implements UseCase, Loggable {

  private final RestClient restClient;
  private final ApplicationEventPublisher publisher;

  public LookupExchangesByApi(RestClient restClient, ApplicationEventPublisher publisher) {
    this.restClient = restClient;
    this.publisher = publisher;
  }

  @Async
  @EventListener
  public void on(BrokerAddedEvent event) {

    RabbitMqBroker broker = event.broker();

    Collection<ExchangeDto> exchanges =
        fetch(
            broker.properties(),
            "exchanges",
            new ParameterizedTypeReference<Collection<ExchangeDto>>() {});

    exchanges.stream()
        .map(ExchangeDto::toRabbitMqExchange)
        .map(exchange -> ExchangeResolvedEvent.from(exchange, event.broker()))
        .forEach(publisher::publishEvent);

    logger()
        .info(
            "Resolved {}",
            fetch(
                broker.properties(), "overview", new ParameterizedTypeReference<OverviewDto>() {}));
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

    public RabbitMqExchange toRabbitMqExchange() {
      return new RabbitMqExchange(null, name);
    }
  }

  record OverviewDto(
      String product_name,
      String product_version,
      String rabbitmq_version,
      String management_version,
      String cluster_name,
      String node,
      String erlang_version,
      String erlang_full_version,
      boolean disable_stats,
      String default_queue_type,
      boolean is_op_policy_updating_enabled,
      boolean enable_queue_totals) {}
}
