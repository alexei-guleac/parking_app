package com.isd.parking.config;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JacksonConfiguration {

    /**
     * Support for Java date and time API.
     *
     * @return the corresponding Jackson module.
     */
    @Bean
    public @NotNull JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public @NotNull Jdk8Module jdk8TimeModule() {
        return new Jdk8Module();
    }

    /**
     * Jackson Afterburner module to speed up serialization/deserialization.
     */
    @Bean
    public @NotNull AfterburnerModule afterburnerModule() {
        return new AfterburnerModule();
    }
}
