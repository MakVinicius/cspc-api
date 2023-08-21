package uol.compass.cspcapi.application.api.scrumMaster.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import uol.compass.cspcapi.application.api.grade.dto.UpdateGradeDTO;

public record UpdateStudentsGradeDTO(
        @Valid
        @NotNull(message = "grades must not be null")
        UpdateGradeDTO grades
) {
}
