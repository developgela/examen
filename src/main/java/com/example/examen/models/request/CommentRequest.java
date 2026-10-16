package com.example.examen.models.request;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotNull(message = "El show_id es requerido")
        Long show_id,

        @NotBlank(message = "El comentario no puede estar vacío")
        String comment,

        @NotNull(message = "La calificación es requerida")
        @Min(value = 0, message = "La calificación mínima es 0")
        @Max(value = 5, message = "La calificación máxima es 5")
        Integer rating
) {}
