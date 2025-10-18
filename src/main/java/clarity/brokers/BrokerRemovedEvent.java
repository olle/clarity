package clarity.brokers;

import clarity.brokers.BrokerRepository.BrokerEntity;

public record BrokerRemovedEvent(Broker removed) {

  public static BrokerRemovedEvent from(BrokerEntity removed) {
    return new BrokerRemovedEvent(removed.toModel());
  }
}
