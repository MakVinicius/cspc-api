package uol.compass.cspcapi.application.api.squad.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import java.util.List;

public record UpdateSquadsStudentsDTO (
        @NotEmpty(message = "list of ids must not be empty")
        List<@Range(min = 0, message = "studentsIds must be equal to or greater than 0") Long> studentsIds
) {
}
