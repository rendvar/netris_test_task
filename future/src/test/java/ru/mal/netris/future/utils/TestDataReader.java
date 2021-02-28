package ru.mal.netris.future.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.InputStream;
import java.util.List;

public class TestDataReader {

    private static final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
            .failOnUnknownProperties(false)
            .build();

    @SneakyThrows
    public static <T> T readObject(String name, Class<T> type) {
        return objectMapper.readValue(readTestDataFile(name), type);
    }

    @SneakyThrows
    public static <T> T readObject(String name, TypeReference<T> type) {
        return objectMapper.readValue(readTestDataFile(name), type);
    }

    @SneakyThrows
    public static <T> List<T> readObjectList(String name, Class<T> type) {
        JavaType valueType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
        return objectMapper.readValue(readTestDataFile(name), valueType);
    }

    private static InputStream readTestDataFile(String name) {
        return TestDataReader.class.getClassLoader().getResourceAsStream("json/" + name + ".json");
    }
}
