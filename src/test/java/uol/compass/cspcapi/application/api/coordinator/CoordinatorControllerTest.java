package uol.compass.cspcapi.application.api.coordinator;

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
import uol.compass.cspcapi.application.api.coordinator.dto.CreateCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.UpdateCoordinatorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.instructor.Instructor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.CoordinatorsConstants.*;

@ExtendWith(MockitoExtension.class)
public class CoordinatorControllerTest {
    @Mock
    private CoordinatorService coordinatorService;

    @InjectMocks
    private CoordinatorController coordinatorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCoordinator_Success() {
        CreateUserDTO userDTO = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second", "linkedInLink");
        CreateCoordinatorDTO coordinatorDTO = new CreateCoordinatorDTO(userDTO);

        ResponseUserDTO expectedUserDTO = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com", "linkedInLink");
        ResponseCoordinatorDTO expectedCoordinator = new ResponseCoordinatorDTO(1L, expectedUserDTO);

        when(coordinatorService.save(any(CreateCoordinatorDTO.class))).thenReturn(expectedCoordinator);

        ResponseEntity<ResponseCoordinatorDTO> responseEntity = coordinatorController.createCoordinator(coordinatorDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedCoordinator.id(), responseEntity.getBody().id());
        assertEquals(expectedCoordinator.user(), responseEntity.getBody().user());
    }

    @Test
    public void testCreateCoordinator_Error() {
        CreateUserDTO userDTO = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second", "linkedInLink");
        CreateCoordinatorDTO coordinatorDTO = new CreateCoordinatorDTO(userDTO);

        when(coordinatorService.save(any(CreateCoordinatorDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving coordinator"));

        assertThrows(RuntimeException.class, () -> coordinatorController.createCoordinator(coordinatorDTO));
    }

    @Test
    public void testGetCoordinatorById_Success() {
        Long coordinatorId = 1L;
        ResponseUserDTO expectedCreateUserDTO = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com", "linkedInLink");
        ResponseCoordinatorDTO expectedCoordinator = new ResponseCoordinatorDTO(coordinatorId, expectedCreateUserDTO);

        when(coordinatorService.getById(anyLong())).thenReturn(expectedCoordinator);

        ResponseEntity<ResponseCoordinatorDTO> responseEntity = coordinatorController.getCoordinatorById(coordinatorId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCoordinator.id(), responseEntity.getBody().id());
        assertEquals(expectedCoordinator.user(), responseEntity.getBody().user());
    }

    @Test
    public void testGetCoordinatorById_NotFound() {
        Long coordinatorId = 999L;

        when(coordinatorService.getById(anyLong())).thenReturn(null);

        try {
            coordinatorController.getCoordinatorById(coordinatorId);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllCoordinators_Success() {
        List<ResponseCoordinatorDTO> expectedCoordinators = new ArrayList<>();
        expectedCoordinators.addAll(List.of(RESPONSE_COORDINATOR_1, RESPONSE_COORDINATOR_2, RESPONSE_COORDINATOR_3));

        when(coordinatorService.getAll()).thenReturn(expectedCoordinators);

        ResponseEntity<List<ResponseCoordinatorDTO>> responseEntity = coordinatorController.getAllCoordinators();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCoordinators.size(), responseEntity.getBody().size());
        assertEquals(expectedCoordinators, responseEntity.getBody());
    }

    @Test
    public void testGetAllCoordinators_EmptyList() {
        List<ResponseCoordinatorDTO> expectedCoordinators = new ArrayList<>();

        when(coordinatorService.getAll()).thenReturn(expectedCoordinators);

        ResponseEntity<List<ResponseCoordinatorDTO>> responseEntity = coordinatorController.getAllCoordinators();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCoordinators.size(), responseEntity.getBody().size());
        assertEquals(expectedCoordinators, responseEntity.getBody());
    }

    @Test
    public void testUpdateCoordinator_Success() {
        Long coordinatorId = 1L;
        UpdateUserDTO userDTO = new UpdateUserDTO(
                RESPONSE_USER_1.firstName(),
                RESPONSE_USER_1.lastName(),
                RESPONSE_USER_1.email(),
                "password",
                "linkedInLink"
        );
        UpdateCoordinatorDTO coordinatorDTO = new UpdateCoordinatorDTO(userDTO);

        Coordinator updatedCoordinator = new Coordinator();
        updatedCoordinator.setId(coordinatorId);
        updatedCoordinator.setUser(USER_1);

        when(coordinatorService.update(anyLong(), any(UpdateCoordinatorDTO.class))).thenReturn(RESPONSE_COORDINATOR_1);

        ResponseEntity<ResponseCoordinatorDTO> responseEntity = coordinatorController.updateCoordinator(coordinatorId, coordinatorDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedCoordinator.getId(), responseEntity.getBody().id());
        assertEquals(updatedCoordinator.getUser().getFirstName(), responseEntity.getBody().user().firstName());
        assertEquals(updatedCoordinator.getUser().getLastName(), responseEntity.getBody().user().lastName());
        assertEquals(updatedCoordinator.getUser().getEmail(), responseEntity.getBody().user().email());
    }

    @Test
    public void testUpdateCoordinator_NotFound() {
        Long coordinatorId = 999L;

        UpdateUserDTO userDTO = new UpdateUserDTO(
                RESPONSE_USER_1.firstName(),
                RESPONSE_USER_1.lastName(),
                RESPONSE_USER_1.email(),
                "password",
                "linkedInLink"
        );
        UpdateCoordinatorDTO coordinatorDTO = new UpdateCoordinatorDTO(userDTO);

        when(coordinatorService.update(anyLong(), any(UpdateCoordinatorDTO.class))).thenReturn(null);

        try {
            coordinatorController.updateCoordinator(coordinatorId, coordinatorDTO);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        Long coordinatorId = 1L;

        doNothing().when(coordinatorService).deleteById(coordinatorId);

        ResponseEntity<Void> responseEntity = coordinatorController.deleteCoordinatorById(coordinatorId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        Long coordinatorId = 999L;

        doThrow(ResponseStatusException.class).when(coordinatorService).deleteById(coordinatorId);

        try {
            coordinatorController.deleteCoordinatorById(coordinatorId);
        } catch (ResponseStatusException exception) {
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }
}
