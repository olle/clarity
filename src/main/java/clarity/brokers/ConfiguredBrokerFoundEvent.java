package clarity.brokers;

import clarity.brokers.BrokersConfigurationProperty.ConfiguredBroker;
import clarity.infrastructure.DomainEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Instant;
import java.util.UUID;

/**
 * Emitted after the fact that a configured broker was resolved or found, this means that a broker
 * configuration was provided, in which case such a pre-configuration was just detected.
 */
public record ConfiguredBrokerFoundEvent(RabbitMqBroker broker, UUID uuid, Instant timestamp)
    implements DomainEvent {

  public static ConfiguredBrokerFoundEvent from(ConfiguredBroker configuredBroker) {
    return new ConfiguredBrokerFoundEvent(
        configuredBroker.toRabbitMqBroker(), UuidCreator.getTimeOrderedEpoch(), Instant.now());
  }
}
