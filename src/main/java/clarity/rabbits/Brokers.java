package clarity.rabbits;

import clarity.utils.Loggable;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Brokers implements Loggable {

  private final BrokerRepository brokerRepository;

  public Brokers(BrokerRepository brokerRepository) {
    this.brokerRepository = brokerRepository;
  }

  @GetMapping(path = "/api/v0/brokers")
  public BrokersDto fetch() {
    return new BrokersDto(brokerRepository.findAll());
  }

  @PostMapping(path = "/api/v0/brokers")
  public void save(BrokerDto broker) {
    brokerRepository.save(broker);
  }

  record BrokersDto(List<Broker> elements) {}

  record BrokerDto(String host, int port, String username, String password, boolean ssl) {}
}
