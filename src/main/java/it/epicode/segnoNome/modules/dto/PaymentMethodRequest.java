package it.epicode.segnoNome.modules.dto;

import it.epicode.segnoNome.auth.entities.AppUser;
import it.epicode.segnoNome.modules.enums.PaymentType;

public class PaymentMethodRequest {
    private AppUser user;

    private String cardNumber;
    private String expirationDate;
    private String cardHolderName;
    private String cvv;
    private PaymentType type;
}
