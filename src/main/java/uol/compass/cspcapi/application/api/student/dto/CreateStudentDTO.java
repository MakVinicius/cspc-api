package uol.compass.cspcapi.application.api.student.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;

public record CreateStudentDTO (
        @Valid
        @NotNull(message = "user must not be null")
        CreateUserDTO user
) {
}
