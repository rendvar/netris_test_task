package ru.mal.netris.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AggregationDto implements Serializable {
    private static final long serialVersionUID = -9202339209693781927L;

    private Long id;
    private LinkType urlType;
    private String videoUrl;
    private String value;
    private Long ttl;
}
