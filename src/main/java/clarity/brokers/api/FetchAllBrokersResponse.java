package clarity.brokers.api;

import java.util.List;

record FetchAllBrokersResponse(int count, List<BrokerDto> elements) {}
