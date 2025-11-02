package clarity.brokers.api;

import clarity.infrastructure.UseCase;
import java.util.UUID;

public interface ActivateUseCase extends UseCase {
  void execute(UUID brokerId);
}
