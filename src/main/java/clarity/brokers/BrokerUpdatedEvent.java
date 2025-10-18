package clarity.brokers;

import clarity.brokers.BrokerRepository.BrokerEntity;

/** Emitted after the fact that a broker entity was updated and persisted. */
public record BrokerUpdatedEvent(Broker current, Broker previous) {

  public static BrokerUpdatedEvent from(BrokerEntity curr, BrokerEntity prev) {
    return new BrokerUpdatedEvent(curr.toModel(), prev.toModel());
  }
}
