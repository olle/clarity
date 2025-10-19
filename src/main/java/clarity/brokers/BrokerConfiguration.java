package clarity.brokers;

import clarity.brokers.BrokersConfigurationProperty.ConfiguredBroker;
import clarity.infrastructure.utils.Loggable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(BrokersConfigurationProperty.class)
public class BrokerConfiguration implements Loggable {

  private final BrokerRepository repo;
  private final BrokersConfigurationProperty configured;
  private final ApplicationEventPublisher applicationEventPublisher;

  public BrokerConfiguration(
      BrokersConfigurationProperty configured,
      ApplicationEventPublisher applicationEventPublisher,
      BrokerRepository repo) {
    this.repo = repo;
    this.configured = configured;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Scheduled(initialDelayString = "10s", fixedDelayString = "PT24H")
  public void onReady() {
    for (ConfiguredBroker broker : configured.getBrokers()) {
      applicationEventPublisher.publishEvent(ConfiguredBrokerFoundEvent.from(broker));
      repo.save(broker);
    }
  }
}
