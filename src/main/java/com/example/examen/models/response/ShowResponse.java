package com.example.examen.models.response;

import java.util.List;

public record ShowResponse(Long id,
                           String name,
                           String channel,
                           String summary,
                           List<String> genres) {
}
