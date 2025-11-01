package clarity.brokers.api;

import java.util.List;
import java.util.UUID;

record FetchAllBrokersResponse(int count, List<BrokerDto> elements, List<UUID> removed) {}
