package uol.compass.cspcapi.application.api.squad.dto;

import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;

import java.util.List;

public record ResponseSquadDTO (
        Long id,
        String name,
        List<ResponseStudentDTO> students
) {
}
