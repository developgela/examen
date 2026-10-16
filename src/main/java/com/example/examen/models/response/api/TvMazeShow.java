package com.example.examen.models.response.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
    public record TvMazeShow(
            Long id,
            String name,
            List<String> genres,
            Network network,
            WebChannel webChannel,
            String summary
    ) {}
