package it.epicode.segnoNome.modules.entities;

import it.epicode.segnoNome.auth.entities.AppUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;


@Entity
@Table(name = "lessons_interests")
@Data
public class LessonInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

   private String firstName;


    private String lastName;


    private String contactInfo;


    private String lessonType; // "Online", "In presenza", "Entrambe"

    @ElementCollection
    private List<String> availableDays; // Lunedì, Martedì, ecc.

    @ElementCollection
    private List<LocalTime> availableTimeSlots; // Fasce orarie disponibili

    @Column(length = 2000)
    private String notes; // Modificabile solo dal Creator

    @Column
    private Boolean contacted; // Se il Creator ha chiamato l'utente

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private AppUser creator; // Chi gestisce la richiesta
}