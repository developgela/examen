package com.example.examen.models.response.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
    public record TvMazeSearchResponse(
            double score,
            TvMazeShow show
    ) {}
