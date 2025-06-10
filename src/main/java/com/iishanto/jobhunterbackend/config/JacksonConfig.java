package com.iishanto.jobhunterbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Timestamp.class, new com.fasterxml.jackson.databind.ser.std.StdSerializer<Timestamp>(Timestamp.class) {
            private final DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .withZone(ZoneId.of("UTC"));

            @Override
            public void serialize(Timestamp value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {
                if (value == null) {
                    gen.writeNull();
                } else {
                    ZonedDateTime zdt = ZonedDateTime.ofInstant(value.toInstant(), ZoneId.of("UTC"));
                    gen.writeString(formatter.format(zdt));
                }
            }
        });

        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        return objectMapper;
    }
}