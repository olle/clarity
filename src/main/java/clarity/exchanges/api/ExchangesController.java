package clarity.exchanges.api;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangesController {

  @GetMapping(path = "/api/v0/exchanges")
  public ExchangesDto fetch() {
    return ExchangesDto.from(List.of());
  }
}
