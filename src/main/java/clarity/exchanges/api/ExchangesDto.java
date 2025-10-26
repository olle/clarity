package clarity.exchanges.api;

import java.util.List;

public record ExchangesDto(int count, List<?> elements) {

  public static ExchangesDto from(List<Object> elements) {
    return new ExchangesDto(elements.size(), elements);
  }
}
