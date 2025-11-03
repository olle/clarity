package clarity.brokers.api;

import java.util.UUID;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ActivateBrokerRequest(UUID id) {

  public ActivateBrokerRequest validate(UUID pathVariable) {
    Assert.isTrue(pathVariable.equals(this.id), "Must be activation for same broker.");
    return this;
  }
}
