package ru.mal.netris.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class CameraDetailsDto implements Serializable {
    private static final long serialVersionUID = 8834693328045438297L;

    private Long id;
    private String sourceDataUrl;
    private String tokenDataUrl;
}
