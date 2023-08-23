package uol.compass.cspcapi.domain.classroom;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomElementsDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.grade.Grade;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;
import uol.compass.cspcapi.domain.user.User;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.ClassroomsConstants.*;
import static uol.compass.cspcapi.commons.StudentsConstants.*;
import static uol.compass.cspcapi.commons.CoordinatorsConstants.*;
import static uol.compass.cspcapi.commons.SquadsConstants.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {
    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private CoordinatorService coordinatorService;

    @Mock
    private StudentService studentService;
    @Mock
    private InstructorService instructorService;
    @Mock
    private ScrumMasterService scrumMasterService;
    @Mock
    private SquadService squadService;


    @InjectMocks
    private ClassroomService classroomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveClassroom() {
        Long coordinatorId = 1L;
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO("Classroom 101", coordinatorId, null);

        when(classroomRepository.findByTitle(classroomDTO.title())).thenReturn(Optional.empty());
        when(classroomRepository.save(any(Classroom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Coordinator coordinator = VALID_COORDINATOR;
        when(coordinatorService.getByIdOriginal(coordinatorId)).thenReturn(coordinator);

        ResponseClassroomDTO result = classroomService.saveClassroom(classroomDTO, coordinatorId);

        verify(classroomRepository).findByTitle(classroomDTO.title());
        verify(classroomRepository).save(any(Classroom.class));
        verify(coordinatorService).getByIdOriginal(coordinatorId);

        assertNotNull(result);

        assertEquals(classroomDTO.title(), result.title());
    }

    @Test
    void testSaveClassroomWithTitleAlreadyExists() {
        Long coordinatorId = 1L;
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO("Classroom 101", coordinatorId, null);

        when(classroomRepository.findByTitle(classroomDTO.title())).thenReturn(Optional.of(new Classroom()));

        assertThrows(ResponseStatusException.class, () -> classroomService.saveClassroom(classroomDTO, coordinatorId));

        verify(classroomRepository).findByTitle(classroomDTO.title());
        verify(classroomRepository, never()).save(any(Classroom.class));
        verify(coordinatorService, never()).getByIdOriginal(coordinatorId);
    }

    @Test
    void testGetById_ExistingClassroom() {
        Long classroomId = 1L;
        Classroom existingClassroom = new Classroom("Classroom 101", new Coordinator());
        existingClassroom.setId(classroomId);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(existingClassroom));

        ResponseClassroomDTO result = classroomService.getById(classroomId);

        verify(classroomRepository).findById(classroomId);

        assertNotNull(result);

        assertEquals(classroomId, result.id());
    }

    @Test
    void testGetById_NonExistingClassroom() {
        Long classroomId = 1L;

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.getById(classroomId));

        verify(classroomRepository).findById(classroomId);
    }

    @Test
    void testGetAllClassrooms() {
        Classroom classroom1 = new Classroom("Classroom 101", new Coordinator());
        classroom1.setId(1L);
        Classroom classroom2 = new Classroom("Classroom 102", new Coordinator());
        classroom2.setId(2L);

        when(classroomRepository.findAll()).thenReturn(Arrays.asList(classroom1, classroom2));

        List<ResponseClassroomDTO> result = classroomService.getAllClassrooms();

        verify(classroomRepository).findAll();

        assertNotNull(result);

        assertEquals(2, result.size());

        assertEquals(classroom1.getId(), result.get(0).id());
        assertEquals(classroom2.getId(), result.get(1).id());
    }

    @Test
    public void testUpdateClassroom_Success() {
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO("New Title", 10L, null);

        Coordinator coordinator = new Coordinator(new User("John", "Doe", "john.doe@mail.com", "john.doe"));
        coordinator.setId(10L);

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setTitle("Old Title");

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(coordinatorService.getByIdOriginal(classroomDTO.coordinatorId())).thenReturn(coordinator);
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        ResponseCoordinatorDTO responseCoordinator = new ResponseCoordinatorDTO(10L, new ResponseUserDTO(10L, "John", "Doe", "john.doe@mail.com", null));
        when(coordinatorService.mapToResponseCoordinator(any(Coordinator.class))).thenReturn(responseCoordinator);

        ResponseClassroomDTO response = classroomService.updateClassroom(classroomId, classroomDTO);

        assertEquals(classroomDTO.title(), response.title());
        assertEquals(classroomDTO.coordinatorId(), response.coordinator().id());
    }

    @Test
    void testUpdateClassroom_NonExistingClassroom() {
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO("Updated Classroom", 2L, null);

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.updateClassroom(classroomId, classroomDTO));

        verify(classroomRepository).findById(classroomId);
        verify(coordinatorService, never()).getByIdOriginal(classroomDTO.coordinatorId());
        verify(classroomRepository, never()).save(any(Classroom.class));
    }

    @Test
    void testDeleteClassroom_ExistingClassroom() {
        Long classroomId = 1L;

        Classroom existingClassroom = new Classroom("Classroom 101", new Coordinator());
        existingClassroom.setId(classroomId);

        List<Student> students = new ArrayList<>();
        students.add(new Student(new User("John", "Doe", "john.doe@mail.com", "john.doe")));
        students.add(new Student(new User("Jane", "Loyd", "jane.loyd@mail.com", "jane.loyd")));

        List<ScrumMaster> scrumMasters = new ArrayList<>();
        scrumMasters.add(new ScrumMaster(new User("Victor", "Silva", "victor.silva@mail.com", "victor.silva")));
        scrumMasters.add(new ScrumMaster(new User("Gilberto", "Maderos", "gilberto.maderos", "gilberto.maderos")));

        List<Instructor> instructors = new ArrayList<>();
        instructors.add(new Instructor(new User("Instructor", "Employee", "instructor.employee1@mail.com", "instructor.employee1@mail.com")));
        instructors.add(new Instructor(new User("Instructor", "Employee", "instructor.employee2@mail.com", "instructor.employee2")));

        List<Squad> squads = new ArrayList<>();
        squads.add(new Squad("Squad 1"));
        squads.add(new Squad("Squad 2"));

        existingClassroom.setStudents(students);
        existingClassroom.setScrumMasters(scrumMasters);
        existingClassroom.setInstructors(instructors);
        existingClassroom.setSquads(squads);

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.of(existingClassroom));

        classroomService.deleteClassroom(classroomId);

        verify(classroomRepository).findById(classroomId);
        verify(studentService).attributeStudentsToClassroom(null, students);
        verify(scrumMasterService).attributeScrumMastersToClassroom(null, scrumMasters);
        verify(squadService).attributeSquadsToClassroom(null, squads);
        verify(classroomRepository).save(existingClassroom);
        verify(classroomRepository).deleteById(classroomId);
    }

    @Test
    void testDeleteClassroom_NonExistingClassroom() {
        Long classroomId = 1L;

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.deleteClassroom(classroomId));

        verify(classroomRepository).findById(classroomId);
        verify(studentService, never()).attributeStudentsToClassroom(any(), any());
        verify(scrumMasterService, never()).attributeScrumMastersToClassroom(any(), any());
        verify(squadService, never()).attributeSquadsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any(Classroom.class));
        verify(classroomRepository, never()).deleteById(anyLong());
    }
    @Test
    void testAddStudentsToClassroom_ExistingClassroom() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(List.of(101L, 102L, 103L));

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setStudents(new ArrayList<>());

        Student student1 = new Student(new User("John", "John", "john.john@mail.com", "john.john", null));
        Student student2 = new Student(new User("Jane", "Jane", "jane.jane@mail.com", "jane.jane", null));
        Student student3 = new Student(new User("Smith", "Smith", "smith.smith@mail.com", "smith.smith", null));
        List<Student> newStudents = List.of(
                student1,
                student2,
                student3
        );
        ResponseUserDTO responseUserDTO1 = new ResponseUserDTO(
                student1.getUser().getId(),
                student1.getUser().getFirstName(),
                student1.getUser().getLastName(),
                student1.getUser().getEmail(),
                null
        );
        ResponseStudentDTO responseStudentDTO1 = new ResponseStudentDTO(
                student1.getId(),
                responseUserDTO1,
                new Grade(),
                null,
                null
        );

        ResponseUserDTO responseUserDTO2 = new ResponseUserDTO(
                student2.getUser().getId(),
                student2.getUser().getFirstName(),
                student2.getUser().getLastName(),
                student2.getUser().getEmail(),
                null
        );
        ResponseStudentDTO responseStudentDTO2 = new ResponseStudentDTO(
                student2.getId(),
                responseUserDTO2,
                new Grade(),
                null,
                null
        );

        ResponseUserDTO responseUserDTO3 = new ResponseUserDTO(
                student3.getUser().getId(),
                student3.getUser().getFirstName(),
                student3.getUser().getLastName(),
                student3.getUser().getEmail(),
                null
        );
        ResponseStudentDTO responseStudentDTO3 = new ResponseStudentDTO(
                student3.getId(),
                responseUserDTO3,
                new Grade(),
                null,
                null
        );

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(studentService.getAllStudentsById(classroomDTO.generalUsersIds())).thenReturn(newStudents);

        List<ResponseStudentDTO> responseStudents = List.of(
                new ResponseStudentDTO(101L, responseUserDTO1, new Grade(), null, null),
                new ResponseStudentDTO(102L, responseUserDTO2, new Grade(), null, null),
                new ResponseStudentDTO(103L, responseUserDTO3, new Grade(), null, null)
        );
        when(studentService.mapToResponseStudents(newStudents)).thenReturn(responseStudents);

        ResponseClassroomDTO response = classroomService.addStudentsToClassroom(classroomId, classroomDTO);

        verify(classroomRepository, times(1)).findById(classroomId);
        verify(classroomRepository, times(1)).save(classroom);
        verify(studentService, times(1)).getAllStudentsById(classroomDTO.generalUsersIds());
        verify(studentService, times(1)).attributeStudentsToClassroom(classroom, newStudents);
        verify(studentService, times(1)).mapToResponseStudents(newStudents);

        assertNotNull(response);
        assertEquals(3, response.students().size());
    }

    @Test
    void testAddStudentsToClassroom_NonExistingClassroom() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.addStudentsToClassroom(classroomId, classroomDTO));

        verify(classroomRepository).findById(classroomId);
        verify(studentService, never()).getAllStudentsById(any());
        verify(studentService, never()).attributeStudentsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    void testAddScrumMastersToClassroom_ExistingClassroom() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(List.of(101L, 102L, 103L));

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setScrumMasters(new ArrayList<>());

        List<ScrumMaster> newScrumMasters = List.of(
                new ScrumMaster(101L, new User("John", "Doe", "john.doe@mail.com", "john.doe")),
                new ScrumMaster(102L, new User("Jane", "Smith", "jane.smith@mail.com", "jane.smith")),
                new ScrumMaster(103L, new User("Harry", "Potter", "harry.potter@mail.com", "harry.potter"))
        );

        ResponseUserDTO responseUserDTO1 = new ResponseUserDTO(101L, "John", "Doe", "john.doe@mail.com", "john.doe");
        ResponseUserDTO responseUserDTO2 = new ResponseUserDTO(102L, "Jane", "Smith", "jane.smith@mail.com", "jane.smith");
        ResponseUserDTO responseUserDTO3 = new ResponseUserDTO(103L, "Harry", "Potter", "harry.potter@mail.com", "harry.potter");

        Classroom newClassroom = new Classroom();
        newClassroom.setScrumMasters(newScrumMasters);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(scrumMasterService.getAllScrumMastersById(classroomDTO.generalUsersIds())).thenReturn(newScrumMasters);

        List<ResponseScrumMasterDTO> responseScrumMasters = List.of(
                new ResponseScrumMasterDTO(101L, responseUserDTO1, null),
                new ResponseScrumMasterDTO(102L, responseUserDTO2, null),
                new ResponseScrumMasterDTO(103L, responseUserDTO3, null)
        );
        when(scrumMasterService.mapToResponseScrumMasters(newScrumMasters)).thenReturn(responseScrumMasters);

        ResponseClassroomDTO response = classroomService.addScrumMastersToClassroom(classroomId, classroomDTO);

        verify(classroomRepository, times(1)).findById(classroomId);
        verify(classroomRepository, times(1)).save(classroom);
        verify(scrumMasterService, times(1)).getAllScrumMastersById(classroomDTO.generalUsersIds());
        verify(scrumMasterService, times(1)).attributeScrumMastersToClassroom(classroom, newScrumMasters);
        verify(scrumMasterService, times(1)).mapToResponseScrumMasters(newScrumMasters);

        assertNotNull(response);
        assertEquals(3, response.scrumMasters().size());
    }

    @Test
    void testAddScrumMastersToClassroom_NonExistingClassroom() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.addScrumMastersToClassroom(classroomId, classroomDTO));

        verify(classroomRepository).findById(classroomId);
        verify(scrumMasterService, never()).getAllScrumMastersById(any());
        verify(scrumMasterService, never()).attributeScrumMastersToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    void testAddInstructorsToClassroom_ExistingClassroom() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(List.of(101L, 102L, 103L));

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setInstructors(new ArrayList<>());

        List<Instructor> newInstructors = List.of(
                new Instructor(new User("John", "Doe", "john.doe@mail.com", "john.doe")),
                new Instructor(new User("Jane", "Smith", "jane.smith@mail.com", "jane.smith")),
                new Instructor(new User("Harry", "Potter", "harry.potter@mail.com", "harry.potter"))
        );

        ResponseUserDTO responseUserDTO1 = new ResponseUserDTO(101L, "John", "Doe", "john.doe@mail.com", "john.doe");
        ResponseUserDTO responseUserDTO2 = new ResponseUserDTO(102L, "Jane", "Smith", "jane.smith@mail.com", "jane.smith");
        ResponseUserDTO responseUserDTO3 = new ResponseUserDTO(103L, "Harry", "Potter", "harry.potter@mail.com", "harry.potter");

        Classroom newClassroom = new Classroom();
        newClassroom.setInstructors(newInstructors);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(instructorService.getAllInstructorsById(classroomDTO.generalUsersIds())).thenReturn(newInstructors);

        List<ResponseInstructorDTO> responseInstructors = List.of(
                new ResponseInstructorDTO(101L, responseUserDTO1, null),
                new ResponseInstructorDTO(102L, responseUserDTO2, null),
                new ResponseInstructorDTO(103L, responseUserDTO3, null)
        );
        when(instructorService.mapToResponseInstructors(newInstructors)).thenReturn(responseInstructors);

        ResponseClassroomDTO response = classroomService.addInstructorsToClassroom(classroomId, classroomDTO);

        verify(classroomRepository, times(1)).findById(classroomId);
        verify(classroomRepository, times(1)).save(classroom);
        verify(instructorService, times(1)).getAllInstructorsById(classroomDTO.generalUsersIds());
        verify(instructorService, times(1)).attributeInstructorsToClassroom(classroom, newInstructors);
        verify(instructorService, times(1)).mapToResponseInstructors(newInstructors);

        assertNotNull(response);
        assertEquals(3, response.instructors().size());
    }

    @Test
    void testAddInstructorsToClassroom_NonExistingClassroom() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.addInstructorsToClassroom(classroomId, classroomDTO));

        verify(classroomRepository).findById(classroomId);
        verify(instructorService, never()).getAllInstructorsById(any());
        verify(instructorService, never()).attributeInstructorsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    void testAddSquadsToClassroom_ExistingClassroom() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(List.of(101L, 102L, 103L));

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setSquads(new ArrayList<>());

        List<Squad> newSquads = List.of(
                new Squad("SpringForce"),
                new Squad("Modern Bugs"),
                new Squad("Cyberchase")
        );
        Classroom newClassroom = new Classroom();
        newClassroom.setSquads(newSquads);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(squadService.getAllSquadsById(classroomDTO.generalUsersIds())).thenReturn(newSquads);

        List<ResponseSquadDTO> responseSquads = List.of(
                new ResponseSquadDTO(101L, "SpringForce", new ArrayList<>()),
                new ResponseSquadDTO(102L, "Modern Bugs", new ArrayList<>()),
                new ResponseSquadDTO(103L, "Cyberchase", new ArrayList<>())
        );
        when(squadService.mapToResponseSquads(newSquads)).thenReturn(responseSquads);

        ResponseClassroomDTO response = classroomService.addSquadsToClassroom(classroomId, classroomDTO);

        verify(classroomRepository, times(1)).findById(classroomId);
        verify(classroomRepository, times(1)).save(classroom);
        verify(squadService, times(1)).getAllSquadsById(classroomDTO.generalUsersIds());
        verify(squadService, times(1)).attributeSquadsToClassroom(classroom, newSquads);
        verify(squadService, times(1)).mapToResponseSquads(newSquads);

        assertNotNull(response);
        assertEquals(3, response.squads().size());
    }

    @Test
    void testAddSquadsToClassroom_NonExistingClassroom() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.addSquadsToClassroom(classroomId, classroomDTO));

        verify(classroomRepository).findById(classroomId);
        verify(squadService, never()).getAllSquadsById(any());
        verify(squadService, never()).attributeSquadsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testRemoveStudentsFromClassroom_SuccessfulRemoval() {
        Long classroomId = 1L;
        List<Long> studentIdsToRemove = Arrays.asList(101L, 102L, 103L);
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(studentIdsToRemove);

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setStudents(new ArrayList<>());

        List<Student> students = List.of(
                new Student(),
                new Student(),
                new Student(),
                new Student()
        );
        classroom.getStudents().addAll(students);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(studentService.getAllStudentsById(classroomDTO.generalUsersIds())).thenReturn(students.subList(0, 3));

        List<ResponseStudentDTO> responseStudents = List.of(
                new ResponseStudentDTO(101L, new ResponseUserDTO(101L, "Test", "teste", "test.teste@mail.com", null), new Grade(), null, null)
        );

        when(studentService.mapToResponseStudents(classroom.getStudents())).thenReturn(responseStudents);

        ResponseClassroomDTO response = classroomService.removeStudentsFromClassroom(classroomId, classroomDTO);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals(1, response.students().size());

        verify(classroomRepository, times(1)).findById(classroomId);
        verify(studentService, times(1)).getAllStudentsById(classroomDTO.generalUsersIds());
        verify(studentService, times(1)).attributeStudentsToClassroom(eq(null), eq(students.subList(0, 3)));
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    public void testRemoveStudentsFromClassroom_ClassroomNotFound() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.removeStudentsFromClassroom(classroomId, classroomDTO));

        verify(classroomRepository).findById(classroomId);
        verify(studentService, never()).getAllStudentsById(any());
        verify(studentService, never()).attributeStudentsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testRemoveScrumMastersFromClassroom_SuccessfulRemoval() {
        Long classroomId = 1L;
        List<Long> scrumMastersIdsToRemove = Arrays.asList(101L, 102L, 103L);
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(scrumMastersIdsToRemove);

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setScrumMasters(new ArrayList<>());

        List<ScrumMaster> scrumMasters = List.of(
                new ScrumMaster(101L, new User("John", "Fooley", "john.fooley@mail.com", "john.fooley")),
                new ScrumMaster(102L, new User("Jane", "Smith", "jane.smith@mail.com", "jane.smith")),
                new ScrumMaster(103L, new User("Smith", "Sutherland", "smith.sutherland@mail.com", "smith.sutherland")),
                new ScrumMaster(104L, new User("David", "Homeland", "david.homeland@mail.com", "david.homeland"))
        );
        classroom.getScrumMasters().addAll(scrumMasters);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(scrumMasterService.getAllScrumMastersById(classroomDTO.generalUsersIds())).thenReturn(scrumMasters.subList(0, 3));

        List<ResponseScrumMasterDTO> responseScrumMasters = List.of(
                new ResponseScrumMasterDTO(104L, new ResponseUserDTO(101L, "John", "Fooley", "john.fooley@mail.com", null), 201L)
        );

        when(scrumMasterService.mapToResponseScrumMasters(classroom.getScrumMasters())).thenReturn(responseScrumMasters);

        ResponseClassroomDTO response = classroomService.removeScrumMastersFromClassroom(classroomId, classroomDTO);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals(1, response.scrumMasters().size());

        verify(classroomRepository, times(1)).findById(classroomId);
        verify(scrumMasterService, times(1)).getAllScrumMastersById(classroomDTO.generalUsersIds());
        verify(scrumMasterService, times(1)).attributeScrumMastersToClassroom(eq(null), eq(scrumMasters.subList(0, 3)));
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    public void testRemoveScrumMastersFromClassroom_ClassroomNotFound() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.removeScrumMastersFromClassroom(classroomId, classroomDTO));

        verify(classroomRepository).findById(classroomId);
        verify(scrumMasterService, never()).getAllScrumMastersById(any());
        verify(scrumMasterService, never()).attributeScrumMastersToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testRemoveInstructorsFromClassroom_SuccessfulRemoval() {
        Long classroomId = 1L;
        List<Long> instructorsIdsToRemove = Arrays.asList(101L, 102L, 103L);
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(instructorsIdsToRemove);

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setInstructors(new ArrayList<>());

        List<Instructor> instructors = List.of(
                new Instructor(new User("John", "Fooley", "john.fooley@mail.com", "john.fooley", "linkedInLink")),
                new Instructor(new User("Jane", "Smith", "jane.smith@mail.com", "jane.smith")),
                new Instructor(new User("Smith", "Sutherland", "smith.sutherland@mail.com", "smith.sutherland")),
                new Instructor(new User("David", "Homeland", "david.homeland@mail.com", "david.homeland"))
        );
        classroom.getInstructors().addAll(instructors);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(instructorService.getAllInstructorsById(classroomDTO.generalUsersIds())).thenReturn(instructors.subList(0, 3));

        List<ResponseInstructorDTO> responseInstructors = List.of(
                new ResponseInstructorDTO(104L, new ResponseUserDTO(101L, "John", "Fooley", "john.fooley@mail.com", null), 201L)
        );

        when(instructorService.mapToResponseInstructors(classroom.getInstructors())).thenReturn(responseInstructors);

        ResponseClassroomDTO response = classroomService.removeInstructorsFromClassroom(classroomId, classroomDTO);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals(1, response.instructors().size());

        verify(classroomRepository, times(1)).findById(classroomId);
        verify(instructorService, times(1)).getAllInstructorsById(classroomDTO.generalUsersIds());
        verify(instructorService, times(1)).attributeInstructorsToClassroom(eq(null), eq(instructors.subList(0, 3)));
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    public void testRemoveInstructorsFromClassroom_ClassroomNotFound() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.removeInstructorsFromClassroom(classroomId, classroomDTO));

        verify(classroomRepository).findById(classroomId);
        verify(instructorService, never()).getAllInstructorsById(any());
        verify(instructorService, never()).attributeInstructorsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testRemoveSquadsFromClassroom_SuccessfulRemoval() {
        Long classroomId = 1L;
        List<Long> squadsIdsToRemove = Arrays.asList(101L, 102L, 103L);
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(squadsIdsToRemove);

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setSquads(new ArrayList<>());

        List<Squad> squads = List.of(
                new Squad("Springforce"),
                new Squad("Modern Bugs"),
                new Squad("Cyberchase"),
                new Squad("Os JEDPS")
        );
        classroom.getSquads().addAll(squads);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(squadService.getAllSquadsById(classroomDTO.generalUsersIds())).thenReturn(squads.subList(0, 3));

        List<ResponseSquadDTO> responseSquads = List.of(
                new ResponseSquadDTO(104L, "Os JEDPS", new ArrayList<>())
        );

        when(squadService.mapToResponseSquads(classroom.getSquads())).thenReturn(responseSquads);

        ResponseClassroomDTO response = classroomService.removeSquadsFromClassroom(classroomId, classroomDTO);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals(1, response.squads().size());

        verify(classroomRepository, times(1)).findById(classroomId);
        verify(squadService, times(1)).getAllSquadsById(classroomDTO.generalUsersIds());
        verify(squadService, times(1)).attributeSquadsToClassroom(eq(null), eq(squads.subList(0, 3)));
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    public void testRemoveSquadsFromClassroom_ClassroomNotFound() {
        Long classroomId = 1L;
        UpdateClassroomElementsDTO classroomDTO = new UpdateClassroomElementsDTO(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.removeSquadsFromClassroom(classroomId, classroomDTO));

        verify(classroomRepository).findById(classroomId);
        verify(squadService, never()).getAllSquadsById(any());
        verify(squadService, never()).attributeSquadsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testMapToResponseClassroom_WithNullStudentsInstructorsScrumMastersAndSquads_ShouldMapToResponseClassroomDTOWithEmptyLists() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setTitle("Sample Classroom");
        classroom.setCoordinator(new Coordinator(new User("John", "Doe", "john.doe@mail.com", "john.doe")));

        when(coordinatorService.mapToResponseCoordinator(any(Coordinator.class))).thenReturn(
                new ResponseCoordinatorDTO(1L, new ResponseUserDTO(1L, "John", "Doe", "john.doe@mail.com", null))
        );

        ResponseClassroomDTO responseClassroomDTO = classroomService.mapToResponseClassroom(classroom);

        assertNotNull(responseClassroomDTO);
        assertEquals(1L, responseClassroomDTO.id());
        assertEquals("Sample Classroom", responseClassroomDTO.title());
        assertEquals("John", responseClassroomDTO.coordinator().user().firstName());
        assertEquals("Doe", responseClassroomDTO.coordinator().user().lastName());
        assertEquals("john.doe@mail.com", responseClassroomDTO.coordinator().user().email());
        assertNotNull(responseClassroomDTO.students());
        assertTrue(responseClassroomDTO.students().isEmpty());
        assertNotNull(responseClassroomDTO.instructors());
        assertTrue(responseClassroomDTO.instructors().isEmpty());
        assertNotNull(responseClassroomDTO.scrumMasters());
        assertTrue(responseClassroomDTO.scrumMasters().isEmpty());
        assertNotNull(responseClassroomDTO.squads());
        assertTrue(responseClassroomDTO.squads().isEmpty());
    }

    @Test
    public void testMapToResponseClassroom_WithNonNullStudentsInstructorsScrumMastersAndSquads_ShouldMapToResponseClassroomDTOWithNonEmptyLists() {
        Classroom classroom = new Classroom();
        classroom.setId(2L);
        classroom.setTitle("Another Classroom");
        Coordinator coordinator = new Coordinator(new User("John", "Doe", "john.doe@mail.com", "john.doe"));
        classroom.setCoordinator(coordinator);

        ResponseCoordinatorDTO responseCoordinatorDTO = new ResponseCoordinatorDTO(coordinator.getId(),
                new ResponseUserDTO(
                        coordinator.getUser().getId(),
                        coordinator.getUser().getFirstName(),
                        coordinator.getUser().getLastName(),
                        coordinator.getUser().getEmail(),
                        null
                )
        );

        List<Student> students = Arrays.asList(
                new Student(new User("Virginia", "Montana", "virginia.montana@mail.com", "viginia.montana")),
                new Student(new User("Bob", "Phill", "bob.phill@mail.com", "bob.phill"))
        );
        List<Instructor> instructors = Arrays.asList(
                new Instructor(new User("Eve", "William", "eve.william@mail.com", "eve.william")),
                new Instructor(new User("Michael", "Souza", "michael.souza@mail.com", "michael.souza"))
        );
        List<ScrumMaster> scrumMasters = Arrays.asList(
                new ScrumMaster(new User("Oscar", "Potter", "oscar.pottter@mail.com", "oscar.pottter")),
                new ScrumMaster(new User("Pamela", "Kratkovswky", "pamela.kratkovswky@mail.com", "pamela.kratkovswky"))
        );
        List<Squad> squads = Arrays.asList(new Squad("Squad A"), new Squad("Squad B"));

        classroom.setStudents(students);
        classroom.setInstructors(instructors);
        classroom.setScrumMasters(scrumMasters);
        classroom.setSquads(squads);

        when(coordinatorService.mapToResponseCoordinator(any(Coordinator.class))).thenReturn(responseCoordinatorDTO);

        when(studentService.mapToResponseStudents(students)).thenReturn(
                Arrays.asList(
                        new ResponseStudentDTO(1L, new ResponseUserDTO(1L, "Virginia", "Montana", "virginia.montana@mail.com", null), new Grade(), null, null),
                        new ResponseStudentDTO(2L, new ResponseUserDTO(2L, "Bob", "Phill", "bob.phill@mail.com", null), new Grade(), null, null)
                )
        );

        when(instructorService.mapToResponseInstructors(instructors)).thenReturn(
                Arrays.asList(
                        new ResponseInstructorDTO(3L, new ResponseUserDTO(3L, "Eve", "William", "eve.william@mail.com", null), null),
                        new ResponseInstructorDTO(4L, new ResponseUserDTO(4L, "Michael", "Souza", "michael.souza@mail.com", null), null)
                )
        );

        when(scrumMasterService.mapToResponseScrumMasters(scrumMasters)).thenReturn(
                Arrays.asList(
                        new ResponseScrumMasterDTO(5L, new ResponseUserDTO(5L, "Oscar", "Potter", "oscar.pottter@mail.com", null), null),
                        new ResponseScrumMasterDTO(6L, new ResponseUserDTO(6L, "Pamela", "Kratkovswky", "pamela.kratkovswky@mail.com", null), null)
                )
        );

        when(squadService.mapToResponseSquads(squads)).thenReturn(
                Arrays.asList(new ResponseSquadDTO(7L, "Squad A", new ArrayList<>()), new ResponseSquadDTO(8L, "Squad B", new ArrayList<>()))
        );

        ResponseClassroomDTO responseClassroomDTO = classroomService.mapToResponseClassroom(classroom);

        assertNotNull(responseClassroomDTO);
        assertEquals(2L, responseClassroomDTO.id());
        assertEquals("Another Classroom", responseClassroomDTO.title());
        assertEquals(classroom.getCoordinator().getUser().getEmail(), responseClassroomDTO.coordinator().user().email());
        assertNotNull(responseClassroomDTO.students());
        assertEquals(2, responseClassroomDTO.students().size());
        assertNotNull(responseClassroomDTO.instructors());
        assertEquals(2, responseClassroomDTO.instructors().size());
        assertNotNull(responseClassroomDTO.scrumMasters());
        assertEquals(2, responseClassroomDTO.scrumMasters().size());
        assertNotNull(responseClassroomDTO.squads());
        assertEquals(2, responseClassroomDTO.squads().size());
    }
}
