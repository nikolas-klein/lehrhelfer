package de.nikolasklein.backend.dto;

public record CardDto(
        Long id,
        String title,
        String content,
        String type,
        int orderIndex
) {}
