package clarity.rabbits;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v0/brokers")
public class Brokers {

  private final BrokerRepository brokerRepository;

  public Brokers(BrokerRepository brokerRepository) {
    this.brokerRepository = brokerRepository;
  }

  @GetMapping
  public BrokersDto fetch() {
    return new BrokersDto(brokerRepository.findAll());
  }

  @PostMapping
  public void save(BrokerDto broker) {
    brokerRepository.save(broker);
  }

  record BrokersDto(List<Broker> elements) {}

  record BrokerDto(String host, int port, String username, String password, boolean ssl) {}
}
