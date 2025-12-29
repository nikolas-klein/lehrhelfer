package de.nikolasklein.backend.service;

import de.nikolasklein.backend.model.Lesson;

import de.nikolasklein.backend.model.User;
import de.nikolasklein.backend.repository.LessonRepository;
import de.nikolasklein.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    // FINDEN (Gibt weiterhin die Entity zurück)
    public Optional<Lesson> getLessonBySlug(String slug) {
        return lessonRepository.findBySlug(slug);
    }

    public List<Lesson> getLessonsByUsername(String username) {
        return lessonRepository.findAllByUserUsername(username);
    }

    // SPEICHERN mit User-Verknüpfung
    @Transactional
    public Lesson saveLessonWithUser(Lesson lesson, String username) {
        // 1. Den User aus der DB holen
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        // 2. Beziehung setzen
        lesson.setUser(user);

        // 3. Karten-Beziehung sicherstellen (Wichtig für JPA Cascade)
        if (lesson.getCards() != null) {
            lesson.getCards().forEach(card -> card.setLesson(lesson));
        }

        return lessonRepository.save(lesson);
    }

    // LÖSCHEN (mit Sicherheitscheck)
    @Transactional
    public void deleteLesson(Long id, String username) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stunde nicht gefunden"));

        // WICHTIG: Prüfen, ob die Stunde wirklich diesem User gehört!
        if (!lesson.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Nicht autorisiert, diese Stunde zu löschen");
        }

        lessonRepository.delete(lesson);
    }
}