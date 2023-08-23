package uol.compass.cspcapi.application.api.squad.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSquadDTO (
        @NotBlank(message = "name must not be empty")
        @Size(min = 3, message = "name must be greater than 3 letters")
        String name
) {
}
