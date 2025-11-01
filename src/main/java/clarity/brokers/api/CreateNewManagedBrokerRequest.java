package clarity.brokers.api;

import clarity.brokers.BrokerType;
import clarity.brokers.RabbitMqBroker;

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
