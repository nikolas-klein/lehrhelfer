package de.nikolasklein.backend.dto;

import java.util.List;

public record LessonDto(
        String title,
        String slug,
        List<CardDto> cards
) {}