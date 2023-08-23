package uol.compass.cspcapi.application.api.classroom.dto;

import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.domain.coordinator.Coordinator;


import java.math.BigDecimal;
import java.util.List;

public record ResponseClassroomDTO (
        Long id,
        String title,
        ResponseCoordinatorDTO coordinator,
        BigDecimal progress,
        List<ResponseStudentDTO> students,
        List<ResponseInstructorDTO> instructors,
        List<ResponseScrumMasterDTO> scrumMasters,
        List<ResponseSquadDTO> squads
) {
}
