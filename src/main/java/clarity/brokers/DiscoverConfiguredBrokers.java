package clarity.brokers;

import clarity.brokers.domain.RabbitMqBroker;
import clarity.brokers.event.ConfiguredBrokerFoundEvent;
import clarity.infrastructure.UseCase;
import clarity.infrastructure.config.BrokersConfigurationProperty;
import clarity.infrastructure.config.BrokersConfigurationProperty.ConfiguredBroker;
import clarity.infrastructure.utils.Loggable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(BrokersConfigurationProperty.class)
public class DiscoverConfiguredBrokers implements Loggable, UseCase {

  private final BrokerRepository repo;
  private final BrokersConfigurationProperty configured;
  private final ApplicationEventPublisher applicationEventPublisher;

  public DiscoverConfiguredBrokers(
      BrokersConfigurationProperty configured,
      ApplicationEventPublisher applicationEventPublisher,
      BrokerRepository repo) {
    this.repo = repo;
    this.configured = configured;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Scheduled(initialDelayString = "11s", fixedDelayString = "PT24H")
  public void onReady() {
    for (ConfiguredBroker configuredBroker : configured.getBrokers()) {
      applicationEventPublisher.publishEvent(ConfiguredBrokerFoundEvent.from(configuredBroker));
      RabbitMqBroker broker = configuredBroker.toRabbitMqBroker();
      if (!repo.exists(broker)) {
        repo.save(broker);
      }
    }
  }
}
