package uol.compass.cspcapi.application.api.instructor.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;

public record ResponseInstructorDTO (
        Long id,
        ResponseUserDTO user,
        Long classroomId
) {
}
