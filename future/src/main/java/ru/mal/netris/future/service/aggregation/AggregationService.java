package ru.mal.netris.future.service.aggregation;

import org.springframework.web.client.HttpStatusCodeException;
import ru.mal.netris.models.AggregationDto;

import java.util.Collection;

public interface AggregationService {

    Collection<AggregationDto> aggregate() throws HttpStatusCodeException;

}
