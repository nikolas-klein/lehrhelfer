package de.nikolasklein.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

    @Entity
    @Getter
    @Setter @NoArgsConstructor @AllArgsConstructor
    public class Lesson {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        @Column(unique = true)
        private String slug;

        @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<Card> cards;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        @JsonBackReference(value = "user-lesson") // Verhindert Endlosschleifen beim User-JSON
        private User user;
    }
