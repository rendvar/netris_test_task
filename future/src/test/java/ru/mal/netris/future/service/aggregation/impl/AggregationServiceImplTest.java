package ru.mal.netris.future.service.aggregation.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import ru.mal.netris.future.service.aggregation.AggregationService;
import ru.mal.netris.future.service.camera.CameraDetailRestService;
import ru.mal.netris.future.utils.TestDataReader;
import ru.mal.netris.models.AggregationDto;
import ru.mal.netris.models.CameraDetailsDto;
import ru.mal.netris.models.CameraSourceDto;
import ru.mal.netris.models.CameraTokenDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class AggregationServiceImplTest {

    @Autowired
    private AggregationService aggregationService;
    @MockBean
    private CameraDetailRestService cameraDetailRestService;


    @Test
    public void should_returnCorrectResult() {
        List<CameraDetailsDto> details = TestDataReader.readObjectList("details", CameraDetailsDto.class);
        Collection<AggregationDto> expected = TestDataReader.readObjectList("result", AggregationDto.class);
        CameraSourceDto source = TestDataReader.readObject("source", CameraSourceDto.class);
        CameraTokenDto token = TestDataReader.readObject("token", CameraTokenDto.class);

        when(cameraDetailRestService.getCameraDetails(anyString())).thenReturn(details);
        when(cameraDetailRestService.getCameraSource(anyString())).thenReturn(source);
        when(cameraDetailRestService.getCameraToken(anyString())).thenReturn(token);

        Collection<AggregationDto> actual = aggregationService.aggregate();

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);
        assertThat(expected).isEqualTo(actual);


        verify(cameraDetailRestService, atLeastOnce()).getCameraDetails(anyString());
        verify(cameraDetailRestService, atLeastOnce()).getCameraSource(anyString());
        verify(cameraDetailRestService, atLeastOnce()).getCameraToken(anyString());
    }

    @Test
    public void should_returnEmptyArray_whenCameraDetailsApiRespondEmptyArray() {
        when(cameraDetailRestService.getCameraDetails(anyString())).thenReturn(Collections.emptyList());

        Collection<AggregationDto> actual = aggregationService.aggregate();

        assertThat(actual).isNotNull();
        assertThat(actual).isEmpty();

        verify(cameraDetailRestService, atLeastOnce()).getCameraDetails(anyString());
        verify(cameraDetailRestService, never()).getCameraSource(anyString());
        verify(cameraDetailRestService, never()).getCameraToken(anyString());
    }

    @Test
    public void should_returnEmptyArray_whenAnyApiRespondWithError() {
        List<CameraDetailsDto> details = TestDataReader.readObjectList("details", CameraDetailsDto.class);
        CameraSourceDto source = TestDataReader.readObject("source", CameraSourceDto.class);

        when(cameraDetailRestService.getCameraDetails(anyString())).thenReturn(details);
        when(cameraDetailRestService.getCameraSource(anyString())).thenReturn(source);
        when(cameraDetailRestService.getCameraToken(anyString())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Collection<AggregationDto> actual = aggregationService.aggregate();

        assertThat(actual).isNotNull();
        assertThat(actual).isEmpty();

        verify(cameraDetailRestService, atLeastOnce()).getCameraDetails(anyString());
        verify(cameraDetailRestService, atLeastOnce()).getCameraSource(anyString());
        verify(cameraDetailRestService, atLeastOnce()).getCameraToken(anyString());
    }
}
