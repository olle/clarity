package clarity.discovery;

import clarity.infrastructure.UseCase;
import clarity.infrastructure.config.BrokersConfigurationProperty;
import clarity.infrastructure.config.BrokersConfigurationProperty.ConfiguredBroker;
import clarity.infrastructure.utils.Loggable;
import clarity.management.BrokerRepository;
import clarity.management.domain.RabbitMqBroker;
import clarity.management.events.ConfiguredBrokerFoundEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(BrokersConfigurationProperty.class)
class DiscoverConfiguredBrokersUseCase implements Loggable, UseCase {

  private final BrokerRepository repo;
  private final BrokersConfigurationProperty configured;
  private final ApplicationEventPublisher applicationEventPublisher;

  public DiscoverConfiguredBrokersUseCase(
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
