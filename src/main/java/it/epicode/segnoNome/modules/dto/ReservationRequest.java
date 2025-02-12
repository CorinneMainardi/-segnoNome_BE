package it.epicode.segnoNome.modules.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRequest {
    @NotNull(message = "the field 'user' cannot be null")
    private Long userId;

    @NotNull(message = "the field 'event' cannot be null")
    private Long eventId;
}