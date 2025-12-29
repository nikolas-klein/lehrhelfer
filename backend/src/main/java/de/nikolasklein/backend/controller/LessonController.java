package de.nikolasklein.backend.controller;

import de.nikolasklein.backend.dto.LessonDto;
import de.nikolasklein.backend.dto.LessonSummaryDto;
import de.nikolasklein.backend.model.Lesson;
import de.nikolasklein.backend.service.LessonService;
import de.nikolasklein.backend.utils.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    // --- ÖFFENTLICHER BEREICH (Schüler / Vorschau) ---

    /**
     * Ermöglicht Schülern den Zugriff auf eine Unterrichtsstunde ohne Login.
     * Nutzt das detaillierte LessonDto inkl. aller Karten.
     */
    @GetMapping("/public/lessons/{slug}")
    public ResponseEntity<LessonDto> getPublicLesson(@PathVariable String slug) {
        return lessonService.getLessonBySlug(slug)
                .map(lessonMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- GESCHÜTZTER BEREICH (Lehrer-Dashboard) ---

    /**
     * Gibt nur die Unterrichtsstunden zurück, die dem aktuell eingeloggten User gehören.
     * Nutzt das schlanke LessonSummaryDto für die Listenansicht.
     */
    @GetMapping("/lessons/my-lessons")
    public List<LessonSummaryDto> getMyLessons(Principal principal) {
        List<Lesson> lessons = lessonService.getLessonsByUsername(principal.getName());
        return lessons.stream()
                .map(lessonMapper::toSummaryDto)
                .toList(); // Java 25 / 16+ Syntax
    }

    /**
     * Erstellt eine neue Unterrichtsstunde und verknüpft sie mit dem aktuellen User.
     */
    @PostMapping("/lessons")
    public ResponseEntity<LessonDto> createLesson(@RequestBody Lesson lesson, Principal principal) {
        Lesson savedLesson = lessonService.saveLessonWithUser(lesson, principal.getName());
        return ResponseEntity.ok(lessonMapper.toDto(savedLesson));
    }

    /**
     * Löscht eine Unterrichtsstunde anhand der ID.
     * Der Service prüft intern, ob die Stunde wirklich dem anfragenden User gehört.
     */
    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id, Principal principal) {
        try {
            lessonService.deleteLesson(id, principal.getName());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Falls der User nicht der Besitzer ist oder die ID nicht existiert
            return ResponseEntity.status(403).build();
        }
    }
}