package uol.compass.cspcapi.application.api.scrummaster;

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
import uol.compass.cspcapi.application.api.scrumMaster.ScrumMasterController;
import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.UpdateScrumMasterDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.ScrumMastersConstants.*;

@ExtendWith(MockitoExtension.class)
public class ScrumMasterControllerTest {
    @Mock
    private ScrumMasterService scrumMasterService;

    @InjectMocks
    private ScrumMasterController scrumMasterController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateScrumMaster_Success() {
        CreateUserDTO user = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second", "linkedInLink");
        CreateScrumMasterDTO scrumMasterDTO = new CreateScrumMasterDTO(user);

        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com", "linkedInLink");
        ResponseScrumMasterDTO expectedScrumMaster = new ResponseScrumMasterDTO(1L, expectedUser, null);

        when(scrumMasterService.save(any(CreateScrumMasterDTO.class))).thenReturn(expectedScrumMaster);

        ResponseEntity<ResponseScrumMasterDTO> responseEntity = scrumMasterController.save(scrumMasterDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedScrumMaster.id(), responseEntity.getBody().id());
        assertEquals(expectedScrumMaster.user(), responseEntity.getBody().user());
    }

    @Test
    public void testCreateScrumMaster_Error() {
        CreateUserDTO user = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second", "linkedInLink");
        CreateScrumMasterDTO scrumMasterDTO = new CreateScrumMasterDTO(user);

        when(scrumMasterService.save(any(CreateScrumMasterDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving scrum master"));

        assertThrows(RuntimeException.class, () -> scrumMasterController.save(scrumMasterDTO));
    }

    @Test
    public void testGetScrumMasterById_Success() {
        Long scrumMasterId = 1L;
        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com", "linkedInLink");
        ResponseScrumMasterDTO expectedScrumMaster = new ResponseScrumMasterDTO(scrumMasterId, expectedUser, null);

        when(scrumMasterService.getById(anyLong())).thenReturn(expectedScrumMaster);

        ResponseEntity<ResponseScrumMasterDTO> responseEntity = scrumMasterController.getById(scrumMasterId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedScrumMaster.id(), responseEntity.getBody().id());
        assertEquals(expectedScrumMaster.user(), responseEntity.getBody().user());
    }

    @Test
    public void testGetScrumMasterById_NotFound() {
        Long scrumMasterId = 999L;

        when(scrumMasterService.getById(anyLong())).thenReturn(null);

        try {
            scrumMasterController.getById(scrumMasterId);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllScrumMasters_Success() {
        List<ResponseScrumMasterDTO> expectedScrumMasters = new ArrayList<>();
        expectedScrumMasters.addAll(List.of(RESPONSE_SCRUMMASTER_1, RESPONSE_SCRUMMASTER_2, RESPONSE_SCRUMMASTER_3));

        when(scrumMasterService.getAll()).thenReturn(expectedScrumMasters);

        ResponseEntity<List<ResponseScrumMasterDTO>> responseEntity = scrumMasterController.getAllScrumMaster();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedScrumMasters.size(), responseEntity.getBody().size());
        assertEquals(expectedScrumMasters, responseEntity.getBody());
    }

    @Test
    public void testGetAllScrumMasters_EmptyList() {
        List<ResponseScrumMasterDTO> expectedScrumMasters = new ArrayList<>();

        when(scrumMasterService.getAll()).thenReturn(expectedScrumMasters);

        ResponseEntity<List<ResponseScrumMasterDTO>> responseEntity = scrumMasterController.getAllScrumMaster();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedScrumMasters.size(), responseEntity.getBody().size());
        assertEquals(expectedScrumMasters, responseEntity.getBody());
    }

    @Test
    public void testUpdateScrumMaster_Success() {
        Long scrumMasterId = 1L;
        UpdateUserDTO user = new UpdateUserDTO(
                RESPONSE_USER_1.firstName(),
                RESPONSE_USER_1.lastName(),
                RESPONSE_USER_1.email(),
                "password",
                "linkedInLink"
        );
        UpdateScrumMasterDTO scrumMasterDTO = new UpdateScrumMasterDTO(user);

        User updatedUser = new User(
                RESPONSE_USER_1.firstName(),
                RESPONSE_USER_1.lastName(),
                RESPONSE_USER_1.email(),
                "password",
                "linkedInLink"
        );
        ScrumMaster updatedScrumMaster = new ScrumMaster();
        updatedScrumMaster.setId(scrumMasterId);
        updatedScrumMaster.setUser(updatedUser);

        when(scrumMasterService.update(anyLong(), any(UpdateScrumMasterDTO.class))).thenReturn(RESPONSE_SCRUMMASTER_1);

        ResponseEntity<ResponseScrumMasterDTO> responseEntity = scrumMasterController.updateScrumMaster(scrumMasterId, scrumMasterDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedScrumMaster.getId(), responseEntity.getBody().id());
        assertEquals(updatedScrumMaster.getUser().getFirstName(), responseEntity.getBody().user().firstName());
        assertEquals(updatedScrumMaster.getUser().getLastName(), responseEntity.getBody().user().lastName());
        assertEquals(updatedScrumMaster.getUser().getEmail(), responseEntity.getBody().user().email());
    }

    @Test
    public void testUpdateScrumMaster_NotFound() {
        Long scrumMasterId = 999L;
        UpdateUserDTO updatedUser = new UpdateUserDTO(
                "First",
                "Second",
                "first.second@mail.com",
                "first.second",
                "linkedInLink"
        );
        UpdateScrumMasterDTO scrumMasterDTO = new UpdateScrumMasterDTO(updatedUser);

        when(scrumMasterService.update(anyLong(), any(UpdateScrumMasterDTO.class))).thenReturn(null);

        try {
            scrumMasterController.updateScrumMaster(scrumMasterId, scrumMasterDTO);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        Long scrumMasterId = 1L;

        doNothing().when(scrumMasterService).delete(scrumMasterId);

        ResponseEntity<Void> responseEntity = scrumMasterController.delete(scrumMasterId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        Long scrumMasterId = 999L;

        doThrow(ResponseStatusException.class).when(scrumMasterService).delete(scrumMasterId);

        try {
            scrumMasterController.delete(scrumMasterId);
        } catch (ResponseStatusException exception) {
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }
}