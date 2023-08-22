package uol.compass.cspcapi.domain.squads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadsStudentsDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadRepository;
import uol.compass.cspcapi.domain.Squad.SquadService;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SquadServiceTest {

    @Mock
    private SquadRepository squadRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SquadService squadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_Save_Success() {
        CreateSquadDTO squadDTO = new CreateSquadDTO("Test Squad");

        when(squadRepository.findByName(squadDTO.name())).thenReturn(Optional.empty());

        Squad newSquad = new Squad(squadDTO.name());
        newSquad.setId(1L);
        when(squadRepository.save(any())).thenReturn(newSquad);

        ResponseSquadDTO result = squadService.save(squadDTO);

        verify(squadRepository).save(any());

        assertEquals(newSquad.getId(), result.id());
        assertEquals(squadDTO.name(), result.name());
    }

    @Test
    public void test_Save_Failure() {
        CreateSquadDTO squadDTO = new CreateSquadDTO("Existing Squad");

        Squad existingSquad = new Squad(squadDTO.name());
        when(squadRepository.findByName(squadDTO.name())).thenReturn(Optional.of(existingSquad));

        assertThrows(ResponseStatusException.class, () -> squadService.save(squadDTO));
    }

    @Test
    public void testUpdateSquad_Success() {
        Long squadId = 1L;
        Squad squad = new Squad();
        squad.setId(squadId);
        squad.setName("Squad 1");

        UpdateSquadDTO squadDTO = new UpdateSquadDTO("Updated Squad");

        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));
        when(squadRepository.save(any())).thenReturn(squad);

        ResponseSquadDTO result = squadService.updateSquad(squadId, squadDTO);

        assertEquals("Updated Squad", result.name());

        verify(squadRepository).findById(squadId);
        verify(squadRepository).save(squad);
    }

    @Test
    public void testUpdateSquad_Failure() {
        UpdateSquadDTO squadDTO = new UpdateSquadDTO("Updated Squad");

        when(squadRepository.findById(any())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> squadService.updateSquad(1L, squadDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Squad not found", exception.getReason());

        verify(squadRepository).findById(1L);
        verify(squadRepository, never()).save(any());
    }

    @Test
    public void testAddStudentsToSquad_Success() {
        Long squadId = 1L;
        UpdateSquadsStudentsDTO squadDTO = new UpdateSquadsStudentsDTO(Arrays.asList(1L, 2L));

        Squad squad = new Squad("Test squad");
        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));

        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);
        when(studentService.getAllStudentsById(squadDTO.studentsIds())).thenReturn(Arrays.asList(student1, student2));

        Student student3 = new Student();
        student3.setId(3L);
        squad.setStudents(List.of(student3));

        when(squadRepository.save(squad)).thenReturn(squad);

        SquadService squadService = new SquadService(squadRepository, studentService);
        ResponseSquadDTO result = squadService.addStudentsToSquad(squadId, squadDTO);

        assertTrue(squad.getStudents().contains(student1));
        assertTrue(squad.getStudents().contains(student2));
        assertTrue(squad.getStudents().contains(student3));

        assertEquals(squad.getId(), result.id());
    }

    @Test
    public void testAddStudentsToSquad_ThrowsResponseStatusException() {
        Long squadId = 1L;
        UpdateSquadsStudentsDTO squadDTO = new UpdateSquadsStudentsDTO(Collections.singletonList(1L));

        when(squadRepository.findById(squadId)).thenReturn(Optional.empty());

        SquadService squadService = new SquadService(squadRepository, studentService);

        assertThrows(ResponseStatusException.class, () -> squadService.addStudentsToSquad(squadId, squadDTO));
    }

    @Test
    public void testRemoveStudentsFromSquad_Success() {
        UpdateSquadsStudentsDTO squadDTO = new UpdateSquadsStudentsDTO(Arrays.asList(1L, 2L));

        Squad squad = new Squad("Test squad");
        squad.setId(1L);

        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);
        Student student3 = new Student();
        student3.setId(3L);

        students.add(student1);
        students.add(student2);
        squad.setStudents(students);

        when(squadRepository.findById(1L)).thenReturn(Optional.of(squad));

        List<Student> toRemoveStudents = Arrays.asList(student1, student2);
        when(studentService.getAllStudentsById(squadDTO.studentsIds())).thenReturn(toRemoveStudents);

        Squad updatedSquad = new Squad("Test squad");
        updatedSquad.setId(1L);
        updatedSquad.setStudents(List.of(student3));

        when(squadRepository.save(squad)).thenReturn(updatedSquad);

        ResponseSquadDTO result = squadService.removeStudentsFromSquad(1L, squadDTO);

        assertEquals(0, squad.getStudents().size());
        assertFalse(squad.getStudents().contains(student1));
        assertFalse(squad.getStudents().contains(student2));

        verify(studentService).attributeStudentsToSquad(null, toRemoveStudents);

        verify(squadRepository).save(squad);

        assertEquals(1L, result.id());
        assertEquals("Test squad", result.name());
    }
    @Test
    public void testRemoveStudentsFromSquad_Failure() {
        UpdateSquadsStudentsDTO squadDTO = new UpdateSquadsStudentsDTO(Arrays.asList(1L, 2L));

        when(squadRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> squadService.removeStudentsFromSquad(1L, squadDTO));
    }

    @Test
    public void testAttributeSquadsToClassroom_Success() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);

        Squad squad1 = new Squad();
        Squad squad2 = new Squad();

        List<Squad> squads = new ArrayList<>();
        squads.add(squad1);
        squads.add(squad2);

        when(squadRepository.saveAll(squads)).thenReturn(squads);

        List<Squad> result = squadService.attributeSquadsToClassroom(classroom, squads);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getClassroom()).isEqualTo(classroom);
        assertThat(result.get(1).getClassroom()).isEqualTo(classroom);
    }

    @Test
    @Rollback
    public void testAttributeSquadsToClassroom_Failure() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);

        Squad squad1 = new Squad();
        Squad squad2 = new Squad();

        List<Squad> squads = new ArrayList<>();
        squads.add(squad1);
        squads.add(squad2);

        when(squadRepository.saveAll(squads)).thenThrow(new RuntimeException("Error saving squads"));

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> squadService.attributeSquadsToClassroom(classroom, squads))
                .withMessageContaining("Error while saving squads");
    }

    @Test
    public void testGetSquadById_Success() {
        Long squadId = 1L;
        Squad squad = new Squad("Test Squad");
        squad.setId(squadId);

        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));

        ResponseSquadDTO result = squadService.getById(squadId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(squadId);
        assertThat(result.name()).isEqualTo("Test Squad");
    }

    @Test
    public void testGetSquadById_Failure() {
        Long squadId = 1L;

        when(squadRepository.findById(squadId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> squadService.getById(squadId))
                .withMessageContaining("squad not found");
    }

    @Test
    public void testGetAll_Success() {
        List<Squad> squads = new ArrayList<>();
        squads.add(new Squad("Squad 1"));
        squads.add(new Squad("Squad 2"));

        when(squadRepository.findAll()).thenReturn(squads);

        List<ResponseSquadDTO> result = squadService.getAll();

        verify(squadRepository).findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllSquads_Failure() {
        Squad squad1 = new Squad("Squad 1");
        Squad squad2 = new Squad("Squad 2");

        when(squadRepository.findAll()).thenReturn(Arrays.asList(squad1, squad2));

        List<ResponseSquadDTO> result = squadService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Squad 1");
        assertThat(result.get(1).name()).isEqualTo("Squad 2");
    }

    @Test
    public void testGetAllSquadsById_Success() {
        List<Long> squadIds = Arrays.asList(1L, 2L);

        List<Squad> squads = Arrays.asList(
                new Squad("Squad 1"),
                new Squad("Squad 2")
        );

        when(squadRepository.findAllByIdIn(squadIds)).thenReturn(squads);

        List<Squad> result = squadService.getAllSquadsById(squadIds);

        assertEquals(squads.size(), result.size());
        squads.forEach(expectedSquad -> assertTrue(result.contains(expectedSquad)));
    }

    @Test
    public void testGetAllSquadsById_Failure() {
        List<Long> squadIds = Arrays.asList(1L, 2L);

        when(squadRepository.findAllByIdIn(squadIds)).thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class, () -> squadService.getAllSquadsById(squadIds));
    }

    @Test
    public void testDeleteSquad_Success() {
        Long squadId = 1L;
        Squad squad = new Squad("Test Squad");
        squad.setId(squadId);

        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));

        squadService.delete(squadId);

        verify(squadRepository).delete(squad);
    }

    @Test
    public void testDeleteSquad_Failure() {
        Long squadId = 1L;

        when(squadRepository.findById(squadId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> squadService.delete(squadId))
                .withMessageContaining("Squad not found");
    }
}
