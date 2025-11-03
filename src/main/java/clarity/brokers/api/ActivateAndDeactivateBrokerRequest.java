package clarity.brokers.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;
import org.springframework.util.Assert;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ActivateAndDeactivateBrokerRequest(UUID id) {

  public ActivateAndDeactivateBrokerRequest validate(UUID pathVariable) {
    Assert.isTrue(pathVariable.equals(this.id), "Must be (de)activation for same broker.");
    return this;
  }
}
