package de.nikolasklein.backend.repository;

import de.nikolasklein.backend.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    // Spring generiert automatisch das SQL, um eine Lesson anhand des Slugs zu finden
    Optional<Lesson> findBySlug(String slug);

    // Findet alle Stunden, die einem User mit diesem Benutzernamen gehören
    List<Lesson> findAllByUserUsername(String username);
}
