package uol.compass.cspcapi.application.api.classroom.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record UpdateClassroomDTO (
        @NotBlank(message = "title must not be empty")
        @Size(min = 3, message = "title must be at least 3 characters long")
        String title,

        @Range(min = 0, message = "coordinatorId must be equal to or greater than 0")
        Long coordinatorId,

        @DecimalMin(value = "0.00", message = "progress can be 0 at minimum")
        @DecimalMax(value = "100.00", message = "progress can be 100 at max")
        BigDecimal progress
) {
}
