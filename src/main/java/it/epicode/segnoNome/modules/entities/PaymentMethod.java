package it.epicode.segnoNome.modules.entities;

import it.epicode.segnoNome.auth.entities.AppUser;
import it.epicode.segnoNome.modules.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payment_methods")
@Data
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    private String cardNumber;
    private String cvv;
    private String expirationDate;
    private String cardHolderName;
    @Enumerated(EnumType.STRING) // 🔹 Usa l'ENUM invece di una stringa
    @Column(nullable = false)
    private PaymentType type;
}
