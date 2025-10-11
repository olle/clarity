package clarity.rabbits;

import static org.assertj.core.api.Assertions.assertThat;

import clarity.rabbits.BrokerRepository.BrokerEntity;
import clarity.rabbits.Brokers.BrokerDto;
import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CreateEntityIdTest {

  @ParameterizedTest
  @CsvSource({
    "pwd,   false,  pwd,  false",
  })
  void ensureEqualsForSameHostnamePortAndUsername(
      String passwordA, boolean sslA, String passwordB, boolean sslB) {

    var a = new BrokerDto("hostname", 1234, "username", passwordA, sslA);
    var b = new BrokerDto("hostname", 1234, "username", passwordB, sslB);

    UUID idA = BrokerEntity.createId(a);
    UUID idB = BrokerEntity.createId(b);

    assertThat(idA).isNotNull().isNotSameAs(idB).isEqualTo(idB);
  }
}
