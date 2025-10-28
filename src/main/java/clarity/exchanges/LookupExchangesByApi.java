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
    String uri = "http://%s:%d/api/exchanges".formatted(props.host(), props.httpPort());
    String basicAuth = encodeBasic(props.username(), props.password());

    logger().info("REQUESTING: {} with {}", uri, basicAuth);

    Collection<ExchangeDto> response =
        restClient
            .get()
            .uri(uri)
            .header("Authorization", basicAuth)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(
                new ParameterizedTypeReference<Collection<ExchangeDto>>() {
                  // OK
                });

    response.forEach(exchange -> logger().info("Resolved: {}", exchange));
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
}
