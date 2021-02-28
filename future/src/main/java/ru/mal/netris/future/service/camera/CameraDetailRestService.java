package ru.mal.netris.future.service.camera;

import ru.mal.netris.models.CameraDetailsDto;
import ru.mal.netris.models.CameraSourceDto;
import ru.mal.netris.models.CameraTokenDto;

import java.util.Collection;

public interface CameraDetailRestService {

    Collection<CameraDetailsDto> getCameraDetails(String url);

    CameraSourceDto getCameraSource(String url);

    CameraTokenDto getCameraToken(String url);

}
