package clarity.brokers.api;

import clarity.brokers.RabbitMqBroker;
import java.util.List;

record BrokersDto(int count, List<BrokerDto> elements) {

  public static BrokersDto from(List<RabbitMqBroker> all) {
    return new BrokersDto(all.size(), all.stream().map(BrokerDto::from).toList());
  }
}
