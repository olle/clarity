package clarity.exchanges.api;

import java.util.List;

record FetchAllExchangeResponse(int count, List<ExchangeDto> elements) {}
