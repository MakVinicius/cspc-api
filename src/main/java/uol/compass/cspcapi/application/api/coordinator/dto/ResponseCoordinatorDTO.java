package uol.compass.cspcapi.application.api.coordinator.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;

public record ResponseCoordinatorDTO (
        Long id,
        ResponseUserDTO user
) {
}
