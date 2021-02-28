package ru.mal.netris.reactive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.mal.netris.models.AggregationDto;
import ru.mal.netris.reactive.service.aggregation.AggregationService;

@RestController
@RequiredArgsConstructor
public class AggregationController {

    private final AggregationService aggregationService;

    @GetMapping("/camera/aggregation")
    public Flux<AggregationDto> aggregate() {
        return aggregationService.aggregate();
    }
}
