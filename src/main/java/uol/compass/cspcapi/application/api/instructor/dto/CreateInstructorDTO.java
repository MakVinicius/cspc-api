package uol.compass.cspcapi.application.api.instructor.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;

public record CreateInstructorDTO (
        @Valid
        @NotNull(message = "user must not be null")
        CreateUserDTO user
) {
}
