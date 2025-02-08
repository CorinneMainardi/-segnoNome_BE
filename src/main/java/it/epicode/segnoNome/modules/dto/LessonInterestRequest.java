package it.epicode.segnoNome.modules.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class LessonInterestRequest {
    @NotBlank (message = "the field 'name' cannot be blank")
    private String firstName;

    @NotBlank (message = "the field 'last name' cannot be blank")
    private String lastName;

    @NotBlank(message = "the field 'contact info' cannot be blank")
    private String contactInfo;

    @NotBlank(message = "the field 'lesson type' cannot be blank")
    private String lessonType; // "Online", "In presenza", "Entrambe"

    @NotEmpty(message = "the field 'avaible days' cannot be empty")
    private List<String> availableDays;

    @NotEmpty(message = "the field 'avaible time' cannot be empty")
    private List<LocalTime> availableTimeSlots;
}
