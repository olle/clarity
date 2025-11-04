package clarity.management.api;

import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
record CreateNewManagedBrokerRequest(String host, Integer port, String username, String password) {

  public RabbitMqBroker toRabbitMqBroker() {
    return new RabbitMqBroker(null, BrokerType.MANAGED).with(host, port, username, password);
  }
}
