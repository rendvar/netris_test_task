package ru.mal.netris.reactive.service.aggregation.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mal.netris.models.AggregationDto;
import ru.mal.netris.models.CameraSourceDto;
import ru.mal.netris.models.CameraTokenDto;
import ru.mal.netris.reactive.config.properties.RestProperties;
import ru.mal.netris.reactive.service.aggregation.AggregationService;
import ru.mal.netris.reactive.service.camera.CameraDetailRestService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AggregationServiceImpl implements AggregationService {

    private final CameraDetailRestService cameraDetailRestService;
    private final RestProperties restProperties;

    @Override
    public Flux<AggregationDto> aggregate() {
        return cameraDetailRestService.getCameraDetails(restProperties.getCameraInfoUrl())
                .onErrorResume(ex -> {
                    log.error("error when get camera details data - {}", ex.getMessage());
                    return Flux.empty();
                })
                .flatMap(details -> {
                    final Mono<CameraSourceDto> cameraSource = cameraDetailRestService.getCameraSource(details.getSourceDataUrl())
                            .onErrorResume(ex -> {
                                log.error("error when get camera source data - {}", ex.getMessage());
                                return Mono.empty();
                            });
                    final Mono<CameraTokenDto> cameraToken = cameraDetailRestService.getCameraToken(details.getTokenDataUrl())
                            .onErrorResume(ex -> {
                                log.error("error when get camera token data - {}", ex.getMessage());
                                return Mono.empty();
                            });

                    return Flux.zip(Mono.just(details.getId()), cameraSource, cameraToken)
                            .map(tuple -> {
                                final CameraSourceDto source = tuple.getT2();
                                final CameraTokenDto token = tuple.getT3();

                                return AggregationDto.builder()
                                        .id(tuple.getT1())
                                        .ttl(token.getTtl())
                                        .value(token.getValue())
                                        .urlType(source.getUrlType())
                                        .videoUrl(source.getVideoUrl())
                                        .build();
                            });
                });
    }
}
