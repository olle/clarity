package clarity.management.domain;

import java.util.UUID;

public interface Broker {

  UUID id();

  BrokerType type();

  String name();
}
