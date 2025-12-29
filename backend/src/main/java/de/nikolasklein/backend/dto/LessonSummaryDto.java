package de.nikolasklein.backend.dto;

public record LessonSummaryDto(
        Long id,
        String title,
        String slug,
        int cardCount
) {}
