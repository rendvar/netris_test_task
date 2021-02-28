package ru.mal.netris.future.service.aggregation.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mal.netris.future.config.properties.RestProperties;
import ru.mal.netris.future.service.aggregation.AggregationService;
import ru.mal.netris.future.service.camera.CameraDetailRestService;
import ru.mal.netris.models.AggregationDto;
import ru.mal.netris.models.CameraDetailsDto;
import ru.mal.netris.models.CameraSourceDto;
import ru.mal.netris.models.CameraTokenDto;
import ru.mal.netris.models.utils.Pair;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AggregationServiceImpl implements AggregationService {

    private final CameraDetailRestService cameraDetailRestService;
    private final RestProperties restProperties;

    @Override
    public Collection<AggregationDto> aggregate() {
        return CompletableFuture.supplyAsync(() -> cameraDetailRestService.getCameraDetails(restProperties.getCameraInfoUrl()))
                .exceptionally(ex -> {
                    log.error("error while load cameras details data - {}", ex.getMessage());
                    return Collections.emptyList();
                })
                .thenApplyAsync(cameraDetails -> {
                    final List<CompletableFuture<Pair<Long, CameraSourceDto>>> sourceFutures = new ArrayList<>(cameraDetails.size());
                    final List<CompletableFuture<Pair<Long, CameraTokenDto>>> tokenFutures = new ArrayList<>(cameraDetails.size());

                    for (CameraDetailsDto details : cameraDetails) {
                        sourceFutures.add(getData(details.getId(), () -> cameraDetailRestService.getCameraSource(details.getSourceDataUrl())));
                        tokenFutures.add(getData(details.getId(), () -> cameraDetailRestService.getCameraToken(details.getTokenDataUrl())));
                    }

                    final CompletableFuture<?>[] futures = Stream.of(sourceFutures, tokenFutures)
                            .flatMap(Collection::stream)
                            .toArray(CompletableFuture[]::new);

                    return CompletableFuture.allOf(futures)
                            .thenApplyAsync(unused -> {
                                final Map<String, CameraSourceDto> sources = mergeMap(sourceFutures);
                                final Map<String, CameraTokenDto> tokens = mergeMap(tokenFutures);

                                return cameraDetails.stream()
                                        .filter(d -> sources.containsKey(d.getId().toString()) && tokens.containsKey(d.getId().toString()))
                                        .map(d -> collectData(d, tokens.get(d.getId().toString()), sources.get(d.getId().toString())))
                                        .collect(Collectors.toList());
                            }).join();
                })
                .exceptionally(ex -> {
                    log.error("error while aggregating camera data - {}", ex.getMessage());
                    return Collections.emptyList();
                })
                .join();
    }

    private <T> CompletableFuture<Pair<Long, T>> getData(Long cameraId, Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier)
                .thenApplyAsync(response -> Pair.of(cameraId, response));
    }

    private <T> Map<String, T> mergeMap(List<CompletableFuture<Pair<Long, T>>> futures) {
        return futures
                .stream()
                .map(CompletableFuture::join)
                .filter(p -> Objects.nonNull(p.getSecond()))
                .collect(Collectors.toMap(p -> p.getFirst().toString(), Pair::getSecond, (v1, v2) -> v1));
    }

    private AggregationDto collectData(CameraDetailsDto detail, CameraTokenDto tokenData, CameraSourceDto sourceData) {
        return AggregationDto.builder()
                .id(detail.getId())
                .ttl(tokenData.getTtl())
                .value(tokenData.getValue())
                .urlType(sourceData.getUrlType())
                .videoUrl(sourceData.getVideoUrl())
                .build();
    }

}
