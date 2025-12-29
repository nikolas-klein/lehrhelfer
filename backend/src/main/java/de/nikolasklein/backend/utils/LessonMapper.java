package de.nikolasklein.backend.utils;

import de.nikolasklein.backend.dto.CardDto;
import de.nikolasklein.backend.dto.LessonDto;
import de.nikolasklein.backend.dto.LessonSummaryDto;
import de.nikolasklein.backend.model.Lesson;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LessonMapper {

    public LessonDto toDto(Lesson lesson) {
        List<CardDto> cardDtos = lesson.getCards().stream()
                .map(card -> new CardDto(
                        card.getId(),
                        card.getTitle(),
                        card.getContent(),
                        card.getType(),
                        card.getOrderIndex()
                ))
                .toList();

        return new LessonDto(lesson.getTitle(), lesson.getSlug(), cardDtos);
    }

    public LessonSummaryDto toSummaryDto(Lesson lesson) {
        return new LessonSummaryDto(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getSlug(),
                lesson.getCards() != null ? lesson.getCards().size() : 0
        );
    }
}