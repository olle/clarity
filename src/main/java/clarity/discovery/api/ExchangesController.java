package clarity.discovery.api;

import clarity.discovery.ExchangeRepository;
import clarity.discovery.domain.RabbitMqExchange;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangesController {

  private final ExchangeRepository repo;

  public ExchangesController(ExchangeRepository repo) {
    this.repo = repo;
  }

  @GetMapping(path = "/api/v0/exchanges")
  public FetchAllExchangesResponse fetch() {
    List<RabbitMqExchange> exchanges = repo.findAll();
    return new FetchAllExchangesResponse(
        exchanges.size(), exchanges.stream().map(ExchangeDto::from).toList());
  }
}
