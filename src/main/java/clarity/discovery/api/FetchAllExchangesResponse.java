package clarity.discovery.api;

import java.util.List;

record FetchAllExchangesResponse(int count, List<ExchangeDto> elements) {}
