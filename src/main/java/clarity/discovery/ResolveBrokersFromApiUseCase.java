package clarity.discovery;

import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import clarity.infrastructure.utils.Utils;
import clarity.management.domain.RabbitMqBroker;
import clarity.management.events.BrokerAddedEvent;
import clarity.management.events.RabbitMqBrokerResolvedEvent;
import java.util.Base64;
import java.util.Optional;
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

    fetch(event.broker(), "overview", new ParameterizedTypeReference<OverviewDto>() {})
        .ifPresent(
            overview -> {
              logger().info("Resolved {}", overview);

              RabbitMqBroker broker =
                  event
                      .broker()
                      .withAttributes(
                          attr ->
                              attr.with("rabbitVersion", overview.rabbitmq_version())
                                  .with("clusterName", overview.cluster_name())
                                  .with("node", overview.node())
                                  .with("erlangVersion", overview.erlang_version())
                                  .with("erlangFullVersion", overview.erlang_full_version()));

              publisher.publishEvent(RabbitMqBrokerResolvedEvent.from(broker));
            });
  }

  private <T> Optional<T> fetch(
      RabbitMqBroker broker, String endpoint, ParameterizedTypeReference<T> type) {
    try {
      return Optional.of(
          restClient
              .get()
              .uri(createUri(broker, endpoint))
              .header("Authorization", encodeBasic(broker.username(), broker.password()))
              .accept(MediaType.APPLICATION_JSON)
              .retrieve()
              .body(type));
    } catch (Exception ex) {
      logger().debug("Failed to fetch broker", ex);
      return Optional.empty();
    }
  }

  private String createUri(RabbitMqBroker broker, String endpoint) {
    return "http://%s:%d/api/%s".formatted(broker.host(), broker.properties().httpPort(), endpoint);
  }

  private String encodeBasic(String username, String password) {
    return "Basic %s"
        .formatted(Base64.getEncoder().encodeToString(Utils.toBytes(username + ":" + password)));
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
