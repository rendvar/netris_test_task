package ru.mal.netris.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class CameraTokenDto implements Serializable {
    private static final long serialVersionUID = -102740972205822959L;

    private String value;
    private Long ttl;
}
