package de.nikolasklein.backend.config;


import de.nikolasklein.backend.model.Card;
import de.nikolasklein.backend.model.Lesson;
import de.nikolasklein.backend.model.User;
import de.nikolasklein.backend.repository.LessonRepository;
import de.nikolasklein.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
public class DataSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(LessonRepository repository) {
        return args -> {
            // 1. Prüfen, ob der Standard-Lehrer existiert, sonst anlegen
            if (userRepository.findByUsername("lehrer").isEmpty()) {
                User admin = new User();
                admin.setUsername("lehrer");
                admin.setPassword(passwordEncoder.encode("geheim")); // Sicher verschlüsselt
                admin.setEmail("admin@lehrhelfer.de");
                admin.setRoles(Set.of("ROLE_ADMIN"));
                userRepository.save(admin);
                System.out.println("👤 Standard-User 'lehrer' wurde angelegt.");
            }
            // Wir prüfen, ob schon Daten da sind, um Dopplungen zu vermeiden
            if (repository.count() == 0) {
                System.out.println("🌱 Seeding database with initial data...");

                // 1. Unterrichtsstunde erstellen
                Lesson mathLesson = new Lesson();
                mathLesson.setTitle("Einführung in Funktionen");
                mathLesson.setSlug("mathe-funktionen-01");
                mathLesson.setCards(new ArrayList<>());

                // 2. Erste Hilfekarte (Text)
                Card card1 = new Card();
                card1.setTitle("Was ist eine Funktion?");
                card1.setContent("Eine Funktion ordnet jedem Element einer Definitionsmenge genau ein Element einer Zielmenge zu.");
                card1.setType("TEXT");
                card1.setOrderIndex(1);
                card1.setLesson(mathLesson); // Wichtig für die Verknüpfung

                // 3. Zweite Hilfekarte (Mathe/LaTeX)
                Card card2 = new Card();
                card2.setTitle("Lineare Funktionsgleichung");
                card2.setContent("f(x) = m \\cdot x + n");
                card2.setType("MATH");
                card2.setOrderIndex(2);
                card2.setLesson(mathLesson);

                // Karten zur Liste der Stunde hinzufügen
                mathLesson.getCards().add(card1);
                mathLesson.getCards().add(card2);
                User admin = userRepository.findByUsername("lehrer").get();
                mathLesson.setUser(admin); // Die Test-Lektion dem Admin zuordnen
                // Speichern (CascadeType.ALL in der Lesson-Klasse sorgt dafür, dass die Karten mitgespeichert werden)
                repository.save(mathLesson);



                System.out.println("✅ Database seeded successfully!");
            } else {
                System.out.println("ℹ️ Database already contains data, skipping seeding.");
            }
        };
    }
}