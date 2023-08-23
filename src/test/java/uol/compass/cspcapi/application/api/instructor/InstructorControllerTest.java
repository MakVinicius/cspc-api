package uol.compass.cspcapi.application.api.instructor;

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
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.InstructorsConstants.*;

@ExtendWith(MockitoExtension.class)
public class InstructorControllerTest {
    @Mock
    private InstructorService instructorService;

    @InjectMocks
    private InstructorController instructorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateInstructor_Success() {
        CreateUserDTO user = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second", "linkedInLink");
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO(user);

        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com", "linkedInLink");
        ResponseInstructorDTO expectedInstructor = new ResponseInstructorDTO(1L, expectedUser, null);

        when(instructorService.save(any(CreateInstructorDTO.class))).thenReturn(expectedInstructor);

        ResponseEntity<ResponseInstructorDTO> responseEntity = instructorController.createInstructor(instructorDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedInstructor.id(), responseEntity.getBody().id());
        assertEquals(expectedInstructor.user(), responseEntity.getBody().user());
    }

    @Test
    public void testCreateInstructor_Error() {
        CreateUserDTO user = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second", "linkedInLink");
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO(user);

        when(instructorService.save(any(CreateInstructorDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving instructor"));

        assertThrows(RuntimeException.class, () -> instructorController.createInstructor(instructorDTO));
    }

    @Test
    public void testGetInstructorById_Success() {
        Long instructorId = 1L;
        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com", "linkedInLink");
        ResponseInstructorDTO expectedInstructor = new ResponseInstructorDTO(instructorId, expectedUser, null);

        when(instructorService.getById(anyLong())).thenReturn(expectedInstructor);

        ResponseEntity<ResponseInstructorDTO> responseEntity = instructorController.getInstructorById(instructorId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedInstructor.id(), responseEntity.getBody().id());
        assertEquals(expectedInstructor.user(), responseEntity.getBody().user());
    }

    @Test
    public void testGetInstructorById_NotFound() {
        Long instructorId = 999L;

        when(instructorService.getById(anyLong())).thenReturn(null);

        try {
            instructorController.getInstructorById(instructorId);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllInstructors_Success() {
        List<ResponseInstructorDTO> expectedInstructors = new ArrayList<>();
        expectedInstructors.addAll(List.of(RESPONSE_INSTRUCTOR_1, RESPONSE_INSTRUCTOR_2, RESPONSE_INSTRUCTOR_3));

        when(instructorService.getAll()).thenReturn(expectedInstructors);

        ResponseEntity<List<ResponseInstructorDTO>> responseEntity = instructorController.getAllInstructors();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedInstructors.size(), responseEntity.getBody().size());
        assertEquals(expectedInstructors, responseEntity.getBody());
    }

    @Test
    public void testGetAllInstructors_EmptyList() {
        List<ResponseInstructorDTO> expectedInstructors = new ArrayList<>();

        when(instructorService.getAll()).thenReturn(expectedInstructors);

        ResponseEntity<List<ResponseInstructorDTO>> responseEntity = instructorController.getAllInstructors();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedInstructors.size(), responseEntity.getBody().size());
        assertEquals(expectedInstructors, responseEntity.getBody());
    }

    @Test
    public void testUpdateInstructor_Success() {
        Long instructorId = 1L;
        UpdateUserDTO user = new UpdateUserDTO(
                RESPONSE_USER_1.firstName(),
                RESPONSE_USER_1.lastName(),
                RESPONSE_USER_1.email(),
                "password",
                "linkedInLink"
        );
        UpdateInstructorDTO instructorDTO = new UpdateInstructorDTO(user);

        User updatedUser = new User(
                RESPONSE_USER_1.firstName(),
                RESPONSE_USER_1.lastName(),
                RESPONSE_USER_1.email(),
                "linkedInLink"
        );
        Instructor updatedInstructor = new Instructor();
        updatedInstructor.setId(instructorId);
        updatedInstructor.setUser(updatedUser);

        when(instructorService.update(anyLong(), any(UpdateInstructorDTO.class))).thenReturn(RESPONSE_INSTRUCTOR_1);

        ResponseEntity<ResponseInstructorDTO> responseEntity = instructorController.updateInstructor(instructorId, instructorDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedInstructor.getId(), responseEntity.getBody().id());
        assertEquals(updatedInstructor.getUser().getFirstName(), responseEntity.getBody().user().firstName());
        assertEquals(updatedInstructor.getUser().getLastName(), responseEntity.getBody().user().lastName());
        assertEquals(updatedInstructor.getUser().getEmail(), responseEntity.getBody().user().email());
    }

    @Test
    public void testUpdateInstructor_NotFound() {
        Long instructorId = 999L;
        UpdateUserDTO user = new UpdateUserDTO("First", "Second", "first.second@mail.com", "first.second", "linkedInLink");
        UpdateInstructorDTO instructorDTO = new UpdateInstructorDTO(user);

        when(instructorService.update(anyLong(), any(UpdateInstructorDTO.class))).thenReturn(null);

        try {
            instructorController.updateInstructor(instructorId, instructorDTO);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        Long instructorId = 1L;

        doNothing().when(instructorService).deleteById(instructorId);

        ResponseEntity<Void> responseEntity = instructorController.deleteInstructorById(instructorId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        Long instructorId = 999L;

        doThrow(ResponseStatusException.class).when(instructorService).deleteById(instructorId);

        try {
            instructorController.deleteInstructorById(instructorId);
        } catch (ResponseStatusException exception) {
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }
}
