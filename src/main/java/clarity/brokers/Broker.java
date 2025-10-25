package clarity.brokers;

import java.util.UUID;

public interface Broker {

  UUID id();

  BrokerType type();
}
