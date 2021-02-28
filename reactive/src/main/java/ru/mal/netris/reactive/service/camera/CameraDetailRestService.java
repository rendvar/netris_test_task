package ru.mal.netris.reactive.service.camera;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mal.netris.models.CameraDetailsDto;
import ru.mal.netris.models.CameraSourceDto;
import ru.mal.netris.models.CameraTokenDto;

public interface CameraDetailRestService {
    Flux<CameraDetailsDto> getCameraDetails(String url);

    Mono<CameraSourceDto> getCameraSource(String url);

    Mono<CameraTokenDto> getCameraToken(String url);
}
