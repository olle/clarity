package clarity.exchanges;

import clarity.brokers.BrokerProperties;
import clarity.brokers.event.BrokerAddedEvent;
import clarity.infrastructure.UseCase;
import clarity.infrastructure.utils.Loggable;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LookupExchangesByApi implements UseCase, Loggable {

  private final RestClient restClient;

  public LookupExchangesByApi(RestClient restClient) {
    this.restClient = restClient;
  }

  @EventListener
  public void on(BrokerAddedEvent event) {

    BrokerProperties props = event.broker().properties();

    Collection<ExchangeDto> response =
        fetch(props, "exchanges", new ParameterizedTypeReference<Collection<ExchangeDto>>() {});
    response.forEach(exchange -> logger().info("Resolved: {}", exchange));

    OverviewDto overview =
        fetch(props, "overview", new ParameterizedTypeReference<OverviewDto>() {});
    logger().info("Resolved {}", overview);
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
      Map<String, Object> arguments) {}

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
