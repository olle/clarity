package clarity.brokers;

import clarity.brokers.api.ActivateUseCase;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class ActivateClarityForBrokerUseCase implements ActivateUseCase {
  public void execute(UUID brokerId) {}
}
