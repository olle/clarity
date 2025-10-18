package clarity.rabbits;

import clarity.rabbits.BrokerRepository.BrokerEntity;

public record BrokerRemovedEvent(Broker removed) {

  public static BrokerRemovedEvent from(BrokerEntity removed) {
    return new BrokerRemovedEvent(removed.toModel());
  }
}
