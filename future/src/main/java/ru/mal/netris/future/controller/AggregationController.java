package ru.mal.netris.future.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mal.netris.future.service.aggregation.AggregationService;
import ru.mal.netris.models.AggregationDto;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class AggregationController {

    private final AggregationService aggregationService;

    @GetMapping("/camera/aggregation")
    public Collection<AggregationDto> aggregate() {
        return aggregationService.aggregate();
    }

}
