package ru.mal.netris.reactive.service.aggregation.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.mal.netris.models.CameraDetailsDto;
import ru.mal.netris.models.CameraSourceDto;
import ru.mal.netris.models.CameraTokenDto;
import ru.mal.netris.models.LinkType;
import ru.mal.netris.reactive.service.aggregation.AggregationService;
import ru.mal.netris.reactive.service.camera.CameraDetailRestService;
import ru.mal.netris.reactive.utils.TestDataReader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class AggregationServiceImplTest {

    @MockBean
    private CameraDetailRestService cameraDetailRestService;
    @Autowired
    private AggregationService aggregationService;

    @Test
    public void should_returnCorrectResult() {
        List<CameraDetailsDto> details = TestDataReader.readObjectList("details", CameraDetailsDto.class);
        CameraSourceDto source = TestDataReader.readObject("source", CameraSourceDto.class);
        CameraTokenDto token = TestDataReader.readObject("token", CameraTokenDto.class);

        when(cameraDetailRestService.getCameraDetails(anyString())).thenReturn(Flux.fromIterable(details));
        when(cameraDetailRestService.getCameraSource(anyString())).thenReturn(Mono.just(source));
        when(cameraDetailRestService.getCameraToken(anyString())).thenReturn(Mono.just(token));

        StepVerifier.create(aggregationService.aggregate())
                .expectNextCount(1)
                .thenConsumeWhile(value -> {
                    assertThat(value).isNotNull();
                    assertThat(value.getId()).isEqualTo(1L);
                    assertThat(value.getTtl()).isEqualTo(180L);
                    assertThat(value.getValue()).isEqualTo("fa4b5f64-249b-11e9-ab14-d663bd873d93");
                    assertThat(value.getUrlType()).isEqualTo(LinkType.LIVE);
                    assertThat(value.getVideoUrl()).isEqualTo("rtsp://127.0.0.1/20");

                    return true;
                })
                .verifyComplete();

        verify(cameraDetailRestService, atLeastOnce()).getCameraDetails(anyString());
        verify(cameraDetailRestService, atLeastOnce()).getCameraSource(anyString());
        verify(cameraDetailRestService, atLeastOnce()).getCameraToken(anyString());
    }

    @Test
    public void should_returnEmptyFlux_whenCameraDetailsApiRespondEmptyArray() {
        when(cameraDetailRestService.getCameraDetails(anyString())).thenReturn(Flux.empty());

        StepVerifier.create(aggregationService.aggregate())
                .expectNextCount(0)
                .verifyComplete();

        verify(cameraDetailRestService, atLeastOnce()).getCameraDetails(anyString());
        verify(cameraDetailRestService, never()).getCameraSource(anyString());
        verify(cameraDetailRestService, never()).getCameraToken(anyString());
    }

    @Test
    public void should_throwsUpException_whenAnyApiRespondWithError() {
        List<CameraDetailsDto> details = TestDataReader.readObjectList("details", CameraDetailsDto.class);
        CameraSourceDto source = TestDataReader.readObject("source", CameraSourceDto.class);

        when(cameraDetailRestService.getCameraDetails(anyString())).thenReturn(Flux.fromIterable(details));
        when(cameraDetailRestService.getCameraSource(anyString())).thenReturn(Mono.just(source));
        when(cameraDetailRestService.getCameraToken(anyString())).thenThrow(new WebClientResponseException(400, "Bad request", null, null, null));

        StepVerifier.create(aggregationService.aggregate())
                .expectNextCount(0)
                .expectError(WebClientResponseException.class)
                .verify();

        verify(cameraDetailRestService, atLeastOnce()).getCameraDetails(anyString());
        verify(cameraDetailRestService, atLeastOnce()).getCameraSource(anyString());
        verify(cameraDetailRestService, atLeastOnce()).getCameraToken(anyString());
    }
}
