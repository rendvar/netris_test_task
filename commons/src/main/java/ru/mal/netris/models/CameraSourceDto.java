package ru.mal.netris.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class CameraSourceDto implements Serializable {
    private static final long serialVersionUID = 1710644847611414730L;

    private LinkType urlType;
    private String videoUrl;
}
