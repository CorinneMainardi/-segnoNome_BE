package it.epicode.segnoNome.modules.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.epicode.segnoNome.auth.entities.AppUser;
import it.epicode.segnoNome.modules.enums.LessonType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Entity
@Table(name = "lesson_interests")
@Data
public class LessonInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;  // Utente che fa la richiesta

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LessonType lessonType;  // ONLINE o IN_PERSON

    @Column(nullable = false)
    private String preferredDays;  // Es. "Lunedì, Mercoledì"

    @Column(nullable = false)
    private String preferredTimes; // Es. "10:00 - 12:00"

    private String city;  // Solo se lezione in presenza

    private boolean contacted = false;  // Se l'utente è stato ricontattato
    private boolean interested = false; // Se è interessato dopo il contatto
    private boolean toBeRecontacted = false; // Se deve essere ricontattato in futuro

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")  // Permette di inserire testi lunghi
    private String note;  // Campo modificabile dal CREATOR
    private boolean handled= false;
}
