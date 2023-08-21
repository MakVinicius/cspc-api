package uol.compass.cspcapi.application.api.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record CreateClassroomDTO (
        @NotBlank(message = "title must not be empty")
        @Size(min = 3, message = "title must be at least 3 characters long")
        String title,

        @NotNull(message = "coordinatorId must not be null")
        @Range(min = 0, message = "coordinatorId must be equal to or greater than 0")
        Long coordinatorId
) {
}
