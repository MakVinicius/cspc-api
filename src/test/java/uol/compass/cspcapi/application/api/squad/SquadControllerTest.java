package uol.compass.cspcapi.application.api.squad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadsStudentsDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.SquadsConstants.*;

@ExtendWith(MockitoExtension.class)
public class SquadControllerTest {

    @Mock
    private SquadService squadService;

    @InjectMocks
    private SquadController squadController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSquad_Success() {
        CreateSquadDTO squadDTO = new CreateSquadDTO("Modern Bugs");

        ResponseSquadDTO expectedSquad = new ResponseSquadDTO(1L, "Modern Bugs", new ArrayList<>());

        when(squadService.save(any(CreateSquadDTO.class))).thenReturn(expectedSquad);

        ResponseEntity<ResponseSquadDTO> responseEntity = squadController.createSquad(squadDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedSquad.id(), responseEntity.getBody().id());
        assertEquals(expectedSquad.name(), responseEntity.getBody().name());
    }

    @Test
    public void testCreateSquad_Error() {
        CreateSquadDTO squadDTO = new CreateSquadDTO("Springforce");

        when(squadService.save(any(CreateSquadDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving squad"));

        assertThrows(RuntimeException.class, () -> squadController.createSquad(squadDTO));
    }

    @Test
    public void testGetSquadById_Success() {
        Long squadId = 1L;
        ResponseSquadDTO expectedSquad = new ResponseSquadDTO(1L, "Modern Bugs", new ArrayList<>());

        when(squadService.getById(anyLong())).thenReturn(expectedSquad);

        ResponseEntity<ResponseSquadDTO> responseEntity = squadController.getSquadById(squadId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedSquad.id(), responseEntity.getBody().id());
        assertEquals(expectedSquad.name(), responseEntity.getBody().name());
    }

    @Test
    public void testGetSquadById_NotFound() {
        Long squadId = 999L;

        when(squadService.getById(anyLong())).thenReturn(null);

        try {
            squadController.getSquadById(squadId);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllSquads_Success() {
        List<ResponseSquadDTO> expectedSquads = new ArrayList<>();
        expectedSquads.addAll(List.of(RESPONSE_SQUAD_1, RESPONSE_SQUAD_2, RESPONSE_SQUAD_3));

        when(squadService.getAll()).thenReturn(expectedSquads);

        ResponseEntity<List<ResponseSquadDTO>> responseEntity = squadController.getAllSquads();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedSquads.size(), responseEntity.getBody().size());
        assertEquals(expectedSquads, responseEntity.getBody());
    }

    @Test
    public void testGetAllSquads_EmptyList() {
        List<ResponseSquadDTO> expectedSquads = new ArrayList<>();

        when(squadService.getAll()).thenReturn(expectedSquads);

        ResponseEntity<List<ResponseSquadDTO>> responseEntity = squadController.getAllSquads();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedSquads.size(), responseEntity.getBody().size());
        assertEquals(expectedSquads, responseEntity.getBody());
    }

    @Test
    public void testUpdateSquad_Success() {
        Long squadId = 1L;
        UpdateSquadDTO squadDTO = new UpdateSquadDTO("Modern Bugs");

        Squad updatedSquad = new Squad();
        updatedSquad.setId(squadId);
        updatedSquad.setName(squadDTO.name());

        ResponseSquadDTO responseSquad = new ResponseSquadDTO(
                1L,
                updatedSquad.getName(),
                new ArrayList<>()
        );

        when(squadService.updateSquad(anyLong(), any(UpdateSquadDTO.class)))
                .thenReturn(responseSquad);

        ResponseEntity<ResponseSquadDTO> responseEntity =
                squadController.updateSquad(squadId, squadDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedSquad.getId(), responseEntity.getBody().id());
        assertEquals(updatedSquad.getName(), responseEntity.getBody().name());
    }

    @Test
    public void testUpdateSquad_NotFound() {
        Long squadId = 999L;
        UpdateSquadDTO squadDTO = new UpdateSquadDTO("Modern Bugs");

        when(squadService.updateSquad(anyLong(), any(UpdateSquadDTO.class))).thenReturn(null);

        try {
            squadController.updateSquad(squadId, squadDTO);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        Long squadId = 1L;

        doNothing().when(squadService).delete(squadId);

        ResponseEntity<Void> responseEntity = squadController.deleteSquadById(squadId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        Long squadId = 999L;

        doThrow(ResponseStatusException.class).when(squadService).delete(squadId);

        try {
            squadController.deleteSquadById(squadId);
        } catch (ResponseStatusException exception) {
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }

    @Test
    public void testAddStudentsToSquad_Success() {
        Long squadId = 1L;
        UpdateSquadsStudentsDTO squadDTO = new UpdateSquadsStudentsDTO(List.of(1L, 2L, 3L));

        ResponseSquadDTO responseSquadDTO = new ResponseSquadDTO(
                squadId,
                "Math Squad",
                List.of(RESPONSE_STUDENT_1, RESPONSE_STUDENT_2, RESPONSE_STUDENT_3)
        );

        when(squadService.addStudentsToSquad(anyLong(), any(UpdateSquadsStudentsDTO.class))).thenReturn(responseSquadDTO);

        ResponseEntity<ResponseSquadDTO> responseEntity =
                squadController.addStudentsToSquad(squadId, squadDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(squadId, responseEntity.getBody().id());
        assertEquals("Math Squad", responseEntity.getBody().name());

        List<Long> studentsIdsResult = new ArrayList<>();
        for (ResponseStudentDTO student : responseEntity.getBody().students()) {
            studentsIdsResult.add(student.id());
        }

        assertEquals(squadDTO.studentsIds(), studentsIdsResult);
    }

    @Test
    public void testAddStudentsToClassroom_ClassroomNotFound() {
        Long squadId = 999L;
        UpdateSquadsStudentsDTO squadDTO = new UpdateSquadsStudentsDTO(List.of(1L, 2L, 3L));

        when(squadService.addStudentsToSquad(anyLong(), any(UpdateSquadsStudentsDTO.class))).thenThrow(ResponseStatusException.class);

        assertThrows(ResponseStatusException.class,
                () -> squadController.addStudentsToSquad(squadId, squadDTO));
    }

    @Test
    public void testRemoveStudentsFromClassroom_Success() {
        Long squadId = 1L;
        UpdateSquadsStudentsDTO squadDTO = new UpdateSquadsStudentsDTO(List.of(101L, 102L, 103L));

        ResponseSquadDTO responseSquadDTO = new ResponseSquadDTO(
                squadId,
                "Math Class",
                Collections.emptyList()
        );

        when(squadService.removeStudentsFromSquad(anyLong(), any(UpdateSquadsStudentsDTO.class))).thenReturn(responseSquadDTO);

        ResponseEntity<ResponseSquadDTO> responseEntity =
                squadController.removeStudentsFromSquad(squadId, squadDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(squadId, responseEntity.getBody().id());
        assertEquals("Math Class", responseEntity.getBody().name());
        assertEquals(Collections.emptyList(), responseEntity.getBody().students());
    }

    @Test
    public void testRemoveStudentsFromClassroom_ClassroomNotFound() {
        Long squadId = 999L;
        UpdateSquadsStudentsDTO squadDTO = new UpdateSquadsStudentsDTO(List.of(101L, 102L, 103L));

        when(squadService.removeStudentsFromSquad(anyLong(), any(UpdateSquadsStudentsDTO.class))).thenThrow(ResponseStatusException.class);

        assertThrows(ResponseStatusException.class,
                () -> squadController.removeStudentsFromSquad(squadId, squadDTO));
    }
}
