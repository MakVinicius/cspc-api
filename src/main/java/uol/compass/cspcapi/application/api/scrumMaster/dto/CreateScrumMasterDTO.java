package uol.compass.cspcapi.application.api.scrumMaster.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.domain.user.User;

public record CreateScrumMasterDTO (
        @Valid
        @NotNull(message = "user must not be null")
        CreateUserDTO user
) {
}
