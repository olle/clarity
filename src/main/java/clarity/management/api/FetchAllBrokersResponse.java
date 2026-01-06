package clarity.management.api;

import clarity.management.domain.RabbitMqBroker;
import java.util.List;
import java.util.UUID;

record FetchAllBrokersResponse(int count, List<BrokerDto> elements, List<UUID> removed) {

  public static FetchAllBrokersResponse from(List<RabbitMqBroker> brokers, List<UUID> removed) {
    return new FetchAllBrokersResponse(
        brokers.size(), brokers.stream().map(BrokerDto::from).toList(), removed);
  }
}
