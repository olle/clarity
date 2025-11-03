package clarity.brokers;

import clarity.brokers.domain.BrokerProperties;
import clarity.brokers.domain.RabbitMqBroker;
import clarity.brokers.event.BrokerAddedEvent;
import clarity.brokers.event.RabbitMqBrokerResolvedEvent;
import clarity.exchanges.ExchangeRepository;
import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import java.util.Base64;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
class ResolveBrokersFromApiUseCase implements Loggable, UseCase {

  private final RestClient restClient;
  private final ApplicationEventPublisher publisher;

  public ResolveBrokersFromApiUseCase(
      RestClient restClient, ExchangeRepository repo, ApplicationEventPublisher publisher) {
    this.restClient = restClient;
    this.publisher = publisher;
  }

  @Async
  @EventListener
  public void on(BrokerAddedEvent event) {

    RabbitMqBroker broker = event.broker();

    OverviewDto overview =
        fetch(broker.properties(), "overview", new ParameterizedTypeReference<OverviewDto>() {});

    logger().info("Resolved {}", overview);

    publisher.publishEvent(
        RabbitMqBrokerResolvedEvent.from(
            broker.withProperties(
                mapper -> mapper.withRabbitMqVersion(overview.rabbitmq_version()))));
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
