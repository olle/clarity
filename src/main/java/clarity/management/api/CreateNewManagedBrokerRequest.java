package clarity.management.api;

import clarity.management.domain.BrokerType;
import clarity.management.domain.RabbitMqBroker;

record CreateNewManagedBrokerRequest(
    String host, Integer port, String username, String password, Boolean ssl) {

  public RabbitMqBroker toRabbitMqBroker() {
    return new RabbitMqBroker(null, BrokerType.MANAGED)
        .withProperties(
            mapper ->
                mapper
                    .withHost(host)
                    .withPort(port)
                    .withUsername(username)
                    .withPassword(password)
                    .withSSL(ssl));
  }
}
