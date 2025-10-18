package clarity.brokers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class BrokerTest {

  @Test
  void ensureMutatesProperties() {

    var broker = new Broker(UUID.randomUUID());

    assertThat(broker).hasFieldOrPropertyWithValue("properties.host", null);
    assertThat(broker).hasFieldOrPropertyWithValue("properties.port", null);
    assertThat(broker).hasFieldOrPropertyWithValue("properties.username", null);
    assertThat(broker).hasFieldOrPropertyWithValue("properties.password", null);
    assertThat(broker).hasFieldOrPropertyWithValue("properties.ssl", false);

    broker.withProperties(
        properties ->
            properties
                .withHost("host")
                .withPort(1234)
                .withUsername("username")
                .withPassword("password")
                .withSSL(true));

    assertThat(broker).hasFieldOrPropertyWithValue("properties.host", "host");
    assertThat(broker).hasFieldOrPropertyWithValue("properties.port", 1234);
    assertThat(broker).hasFieldOrPropertyWithValue("properties.username", "username");
    assertThat(broker).hasFieldOrPropertyWithValue("properties.password", "password");
    assertThat(broker).hasFieldOrPropertyWithValue("properties.ssl", true);
  }
}
