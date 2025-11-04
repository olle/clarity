package clarity.management.api;

import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;
import org.springframework.util.Assert;

@JsonIgnoreProperties(ignoreUnknown = true)
record UpdateManagedBrokerRequest(
    UUID id, String host, Integer port, String username, String password) {

  public UpdateManagedBrokerRequest validate(UUID pathVariable) {
    Assert.isTrue(pathVariable.equals(this.id), "Must be update for same broker.");
    return this;
  }

  public RabbitMqBroker toRabbitMqBroker() {
    return new RabbitMqBroker(id, BrokerType.MANAGED).with(host, port, username, password);
  }
}
