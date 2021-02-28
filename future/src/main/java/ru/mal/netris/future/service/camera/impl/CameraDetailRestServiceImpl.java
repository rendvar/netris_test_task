package ru.mal.netris.future.service.camera.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mal.netris.future.service.camera.CameraDetailRestService;
import ru.mal.netris.models.CameraDetailsDto;
import ru.mal.netris.models.CameraSourceDto;
import ru.mal.netris.models.CameraTokenDto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
public class CameraDetailRestServiceImpl implements CameraDetailRestService {

    private final RestTemplate restTemplate;

    @Override
    public Collection<CameraDetailsDto> getCameraDetails(String url) {
        return getData(url, CameraDetailsDto[].class).map(Arrays::asList).orElse(emptyList());
    }

    @Override
    public CameraSourceDto getCameraSource(String url) {
        return getData(url, CameraSourceDto.class).orElse(null);
    }

    @Override
    public CameraTokenDto getCameraToken(String url) {
        return getData(url, CameraTokenDto.class).orElse(null);
    }

    private <T extends Serializable> Optional<T> getData(String uri, Class<T> type) {
        final ResponseEntity<T> entity = restTemplate.getForEntity(uri, type);
        return entity.getStatusCode().is2xxSuccessful() && entity.hasBody() ? ofNullable(entity.getBody()) : empty();
    }
}
