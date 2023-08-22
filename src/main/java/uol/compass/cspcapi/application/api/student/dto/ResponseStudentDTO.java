package uol.compass.cspcapi.application.api.student.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.grade.Grade;

public record ResponseStudentDTO (
        Long id,
        ResponseUserDTO user,
        Grade grades,
        Long squadId,
        Long classroomId
) {
}
