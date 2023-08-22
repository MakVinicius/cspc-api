package uol.compass.cspcapi.application.api.student.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;

public record UpdateStudentDTO (
        @Valid
        @NotNull(message = "user must not be null")
        UpdateUserDTO user
) {
}
