package ru.mal.netris.reactive.service.aggregation;


import reactor.core.publisher.Flux;
import ru.mal.netris.models.AggregationDto;

public interface AggregationService {

    Flux<AggregationDto> aggregate();

}
