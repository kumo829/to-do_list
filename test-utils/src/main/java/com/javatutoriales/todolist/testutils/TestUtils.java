package com.javatutoriales.todolist.testutils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtils {
    private static ObjectMapper objectMapper;

    static {
        JavaTimeModule module = new JavaTimeModule();

        LocalDateTimeDeserializer localDateTimeDeserializer =
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS'Z'"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        module.addDeserializer(OffsetDateTime.class, new CustomDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("America/Mexico_City"))));

        objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();//new ObjectMapper();

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class CustomDeserializer extends JsonDeserializer<OffsetDateTime> {

        private DateTimeFormatter formatter;

        public CustomDeserializer(DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public OffsetDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {

            ZonedDateTime datetime = ZonedDateTime.parse(parser.getText(), formatter);

            return datetime.toOffsetDateTime();
        }
    }
}