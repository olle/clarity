package clarity.brokers;

import clarity.brokers.BrokerRepository.BrokerEntity;

/** Emitted after the fact that a new broker was added and saved to the repository. */
public record BrokerAddedEvent(Broker broker) {

  public static BrokerAddedEvent from(BrokerEntity entity) {
    return new BrokerAddedEvent(entity.toModel());
  }
}
