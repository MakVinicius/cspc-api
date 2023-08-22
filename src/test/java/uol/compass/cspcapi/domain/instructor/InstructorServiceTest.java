package uol.compass.cspcapi.domain.instructor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleRepository;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.CoordinatorsConstants.*;

public class InstructorServiceTest {

    private static InstructorRepository instructorRepository;
    private static UserRepository userRepository;
    private static RoleRepository roleRepository;
    private static UserService userService;
    private static RoleService roleService;
    private static InstructorService instructorService;
    private static UserService mockUserService;
    private static InstructorService mockInstructorService;

    @BeforeAll()
    public static void setUp(){
        userRepository = mock(UserRepository.class);
        instructorRepository = mock(InstructorRepository.class);
        roleRepository = mock(RoleRepository.class);
        mockUserService = mock(UserService.class);
        mockInstructorService = mock(InstructorService.class);

        userService = new UserService(userRepository, new PasswordEncoder());
        roleService = new RoleService(roleRepository);
//        instructorService = new InstructorService(instructorRepository, userService, new PasswordEncoder(), roleService);
        instructorService = new InstructorService(instructorRepository, mockUserService, new PasswordEncoder(), roleService);
    }

    @AfterEach()
    void down(){
        reset(userRepository);
        reset(roleRepository);
        reset(instructorRepository);
        reset(mockInstructorService);
    }

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void testSave_Success() {
        User user = new User("John", "Doe", "johndoe@compass.com", "password");
        CreateUserDTO userDTO = new CreateUserDTO("John", "Doe", "johndoe@compass.com", "password", "linkedInLink");
        Instructor instructor = new Instructor(user);
        Classroom classroom = new Classroom();
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO(userDTO);

        instructor.setId(1L);
        classroom.setId(1L);
        instructor.setClassroom(classroom);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role("ROLE_INSTRUCTOR")));

        ResponseInstructorDTO result = instructorService.save(instructorDTO);

        assertEquals(instructor.getId(), result.id());
        assertEquals(instructor.getUser().getFirstName(), result.user().firstName());
        assertEquals(instructor.getUser().getLastName(), result.user().lastName());
        assertEquals(instructor.getUser().getEmail(), result.user().email());
    }

    @Test
    public void testSave_Failure() {
        CreateUserDTO userDTO = new CreateUserDTO("John", "Doe", "johndoe@compass.com", "password", "linkedInLink");
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO(userDTO);

        User existingUser = new User("Jane", "Smith", "johndoe@compass.com", "hashed_password");

        when(userService.findByEmail("johndoe@compass.com")).thenReturn(Optional.of(existingUser));

        assertThrows(ResponseStatusException.class, () -> instructorService.save(instructorDTO));
    }


    //Update instructor
    @Test
    public void testUpdate_Success() {
        Long instructorId = 1L;
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        user.setId(1L);
        Instructor instructor = new Instructor(user);
        instructor.setId(instructorId);

        User newUser = new User("User1", "User2", "user@mail.com", "user1.user2");
        newUser.setId(1L);
        Instructor newInstructor = new Instructor(newUser);
        newInstructor.setId(instructorId);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO("User1", "User2", "user@mail.com", "user1.user2", "linkedInLink");
        UpdateInstructorDTO instructorDTO = new UpdateInstructorDTO(updateUserDTO);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(newInstructor);

        ResponseInstructorDTO result = instructorService.update(instructorId, instructorDTO);

        verify(instructorRepository, times(1)).findById(anyLong());
        assertEquals(instructorId, result.id());
        assertEquals(instructorDTO.user().firstName(), result.user().firstName());
        assertEquals(instructorDTO.user().lastName(), result.user().lastName());
        assertEquals(instructorDTO.user().email(), result.user().email());
    }

    @Test
    public void testUpdate_Failure() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("User1", "User2", "user@mail.com", "user1.user2", "linkedInLink");
        UpdateInstructorDTO instructorDTO = new UpdateInstructorDTO(updateUserDTO);

        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> instructorService.update(1L, instructorDTO));
    }


    //GetById
    @Test
    public void testGetById_Success() {
        Long instructorId = 1L;
        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        Instructor existingInstructor = new Instructor(user_1);
        existingInstructor.setId(instructorId);

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(existingInstructor));
        ResponseInstructorDTO result = instructorService.getById(1L);

        verify(instructorRepository).findById(instructorId);
        verify(instructorRepository, times(1)).findById(instructorId);

        assertNotNull(result);
        assertEquals(instructorId, result.id());
    }

    @Test
    public void testGetInstructorById_Failure() {
        Long instructorId = 2L;
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> instructorService.getById(instructorId));
    }


    //GetAll
    @Test
    public void testGetAll_Success() {
        Instructor instructor1 = new Instructor(new User("John", "Doe", "johndoe@compass.com", "senha"));
        Instructor instructor2 = new Instructor(new User("Jane", "Doe", "janedoe@compass.com", "senha"));
        List<Instructor> mockInstructors = Arrays.asList(instructor1, instructor2);

        when(instructorRepository.findAll()).thenReturn(mockInstructors);

        List<ResponseInstructorDTO> result = instructorService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    public void testGetAll_Failure() {
        when(instructorRepository.findAll()).thenReturn(Collections.emptyList());

        instructorService.getAll();
    }


    //GetAllById
    @Test
    public void testGetAllInstructorsById_Success() {
        List<Long> instructorIds = Arrays.asList(1L, 2L, 3L);

        List<Instructor> mockInstructors = Arrays.asList(new Instructor(), new Instructor(), new Instructor());

        when(instructorRepository.findAllByIdIn(instructorIds)).thenReturn(mockInstructors);

        List<Instructor> result = instructorService.getAllInstructorsById(instructorIds);

        assertEquals(mockInstructors, result);
    }

    @Test
    public void testGetAllInstructorsById_Failure() {
        List<Long> instructorIds = Arrays.asList(1L, 2L, 3L);

        when(instructorRepository.findAllByIdIn(instructorIds)).thenReturn(Arrays.asList(new Instructor(), new Instructor()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> instructorService.getAllInstructorsById(instructorIds));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("One or more instructors not found", exception.getReason());
    }



    //DeleteById
    @Test
    public void testDeleteById_Success() {
        Long instructorId = 1L;

        Instructor instructor = new Instructor(new User("John", "Doe", "johndoe@compass.com", "senha"));
        instructor.setId(instructorId);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        instructorService.deleteById(instructorId);

        verify(instructorRepository).delete(instructor);
    }

    @Test
    public void testDeleteById_Failure() {
        Long instructorId = 1L;

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> instructorService.deleteById(instructorId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("instructor not found", exception.getReason());
    }


    //Attribute instructor to classroom
    @Test
    public void testAttributeInstructorsToClassroom_Success() {
        Classroom classroom = new Classroom( "Classroom", COORDINATOR_1);

        List<Instructor> instructors = new ArrayList<>();
        instructors.add(new Instructor(new User("John", "Doe", "john@compass.com", "password", null)));
        instructors.add(new Instructor(new User("Jane", "Smith", "jane@compass.com", "password")));

        when(instructorRepository.saveAll(anyList())).thenReturn(instructors);
        when(instructorRepository.findAllByIdIn(anyList())).thenReturn(instructors);

        List<ResponseInstructorDTO> result = instructorService.attributeInstructorsToClassroom(classroom, instructors);

        for (Instructor instructor : instructors) {
            assertEquals(classroom, instructor.getClassroom());
        }

        assertEquals(instructors.size(), result.size());

        verify(instructorRepository).findAllByIdIn(anyList());
        verify(instructorRepository).saveAll(anyList());
    }

    @Test
    public void testAttributeInstructorsToClassroom_Failure() {
        Classroom classroom = new Classroom( "Classroom", COORDINATOR_1);
        User user = new User("John", "Doe", "test@mail.com", "12344321");
        Instructor i1 = new Instructor(user);
        Instructor i2 = new Instructor(user);

        i1.setId(1L);
        i2.setId(2L);

        List<Instructor> instructors = new ArrayList<>();
        instructors.add(i1);
        instructors.add(i2);

        List<Long> idList = List.of(1L);

        when(instructorRepository.findAllByIdIn(idList)).thenThrow(ResponseStatusException.class);

        assertThrows(ResponseStatusException.class,
                () -> instructorService.attributeInstructorsToClassroom(classroom, instructors)
        );
    }



}







