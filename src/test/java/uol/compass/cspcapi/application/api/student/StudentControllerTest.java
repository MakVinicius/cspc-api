package uol.compass.cspcapi.application.api.student;



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
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.grade.Grade;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.StudentsConstants.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testsave_Success() {
        CreateUserDTO userDTO = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second", "linkedInLink");
        CreateStudentDTO studentDTO = new CreateStudentDTO(userDTO);

        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com", "linkedInLink");
        ResponseStudentDTO expectedStudent = new ResponseStudentDTO(1L, expectedUser, new Grade(), null, null);

        when(studentService.save(any(CreateStudentDTO.class))).thenReturn(expectedStudent);

        ResponseEntity<ResponseStudentDTO> responseEntity = studentController.save(studentDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedStudent.id(), responseEntity.getBody().id());
        assertEquals(expectedStudent.user(), responseEntity.getBody().user());
    }

    @Test
    public void testsave_Error() {
        CreateUserDTO userDTO = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second", "linkedInLink");
        CreateStudentDTO studentDTO = new CreateStudentDTO(userDTO);

        when(studentService.save(any(CreateStudentDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving instructor"));

        assertThrows(RuntimeException.class, () -> studentController.save(studentDTO));
    }

    @Test
    public void testGetStudentById_Success() {
        Long studentId = 1L;
        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com", "linkedInLink");
        ResponseStudentDTO expectedStudent = new ResponseStudentDTO(studentId, expectedUser, new Grade(), null, null);

        when(studentService.getById(anyLong())).thenReturn(expectedStudent);

        ResponseEntity<ResponseStudentDTO> responseEntity = studentController.getById(studentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedStudent.id(), responseEntity.getBody().id());
        assertEquals(expectedStudent.user(), responseEntity.getBody().user());
    }

    @Test
    public void testGetStudentById_NotFound() {
        Long studentId = 999L;

        when(studentService.getById(anyLong())).thenReturn(null);

        try {
            studentController.getById(studentId);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllStudents_Success() {
        List<ResponseStudentDTO> expectedStudents = new ArrayList<>();
        expectedStudents.addAll(List.of(RESPONSE_STUDENT_1, RESPONSE_STUDENT_2, RESPONSE_STUDENT_3));

        when(studentService.getAll()).thenReturn(expectedStudents);

        ResponseEntity<List<ResponseStudentDTO>> responseEntity = studentController.getAllStudents();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedStudents.size(), responseEntity.getBody().size());
        assertEquals(expectedStudents, responseEntity.getBody());
    }

    @Test
    public void testGetAllStudents_EmptyList() {
        List<ResponseStudentDTO> expectedStudents = new ArrayList<>();

        when(studentService.getAll()).thenReturn(expectedStudents);

        ResponseEntity<List<ResponseStudentDTO>> responseEntity = studentController.getAllStudents();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedStudents.size(), responseEntity.getBody().size());
        assertEquals(expectedStudents, responseEntity.getBody());
    }

    @Test
    public void testUpdateStudent_Success() {
        Long studentId = 1L;
        User user = new User(
                RESPONSE_USER_1.firstName(),
                RESPONSE_USER_1.lastName(),
                RESPONSE_USER_1.email(),
                "password",
                "linkedInLink"
        );
        UpdateUserDTO userDTO = new UpdateUserDTO(
                RESPONSE_USER_1.firstName(),
                RESPONSE_USER_1.lastName(),
                RESPONSE_USER_1.email(),
                "password",
                "linkedInLink"
        );
        UpdateStudentDTO studentDTO = new UpdateStudentDTO(userDTO);

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setUser(user);

        when(studentService.update(anyLong(), any(UpdateStudentDTO.class))).thenReturn(RESPONSE_STUDENT_1);

        ResponseEntity<ResponseStudentDTO> responseEntity = studentController.updateStudent(studentId, studentDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedStudent.getId(), responseEntity.getBody().id());
        assertEquals(updatedStudent.getUser().getFirstName(), responseEntity.getBody().user().firstName());
        assertEquals(updatedStudent.getUser().getLastName(), responseEntity.getBody().user().lastName());
        assertEquals(updatedStudent.getUser().getEmail(), responseEntity.getBody().user().email());
    }

    @Test
    public void testUpdateStudent_NotFound() {
        Long studentId = 999L;
        UpdateUserDTO userDTO = new UpdateUserDTO(
                RESPONSE_USER_1.firstName(),
                RESPONSE_USER_1.lastName(),
                RESPONSE_USER_1.email(),
                "password",
                "linkedInLink"
        );
        UpdateStudentDTO studentDTO = new UpdateStudentDTO(userDTO);

        when(studentService.update(anyLong(), any(UpdateStudentDTO.class))).thenReturn(null);

        try {
            studentController.updateStudent(studentId, studentDTO);
        } catch (ResponseStatusException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        Long studentId = 1L;

        doNothing().when(studentService).delete(studentId);

        ResponseEntity<Void> responseEntity = studentController.delete(studentId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        Long studentId = 999L;

        doThrow(ResponseStatusException.class).when(studentService).delete(studentId);

        try {
            studentController.delete(studentId);
        } catch (ResponseStatusException exception) {
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }
}
