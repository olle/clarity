package clarity.discovery.api;

import java.util.List;

record FetchAllExchangeResponse(int count, List<ExchangeDto> elements) {}
