package it.epicode.segnoNome.modules.entities;

import it.epicode.segnoNome.auth.entities.AppUser;
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
    private String type;
}
