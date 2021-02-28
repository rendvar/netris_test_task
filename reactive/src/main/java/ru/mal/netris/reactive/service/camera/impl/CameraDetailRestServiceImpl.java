package ru.mal.netris.reactive.service.camera.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mal.netris.models.CameraDetailsDto;
import ru.mal.netris.models.CameraSourceDto;
import ru.mal.netris.models.CameraTokenDto;
import ru.mal.netris.reactive.service.camera.CameraDetailRestService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CameraDetailRestServiceImpl implements CameraDetailRestService {

    private final WebClient webClient;

    @Override
    public Flux<CameraDetailsDto> getCameraDetails(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(CameraDetailsDto.class);
    }

    @Override
    public Mono<CameraSourceDto> getCameraSource(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CameraSourceDto.class);

    }

    @Override
    public Mono<CameraTokenDto> getCameraToken(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CameraTokenDto.class);
    }
}
