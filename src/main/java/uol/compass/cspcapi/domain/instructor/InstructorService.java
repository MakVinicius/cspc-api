package uol.compass.cspcapi.domain.instructor;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {
    private InstructorRepository instructorRepository;

    private final UserService userService;
    private final PasswordEncoder passwordEncrypt;
    private final RoleService roleService;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, UserService userService, PasswordEncoder passwordEncrypt, RoleService roleService) {
        this.instructorRepository = instructorRepository;
        this.userService = userService;
        this.passwordEncrypt = passwordEncrypt;
        this.roleService = roleService;
    }

    @Transactional
    public ResponseInstructorDTO save(CreateInstructorDTO instructor) {
        Optional<User> alreadyExists = userService.findByEmail(instructor.user().email());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        User user = new User(
                instructor.user().firstName(),
                instructor.user().lastName(),
                instructor.user().email(),
                passwordEncrypt.encoder().encode(instructor.user().password()),
                instructor.user().linkedInLink()
        );

        user.getRoles().add(roleService.findRoleByName("ROLE_INSTRUCTOR"));

        Instructor newInstructor = new Instructor(user);
        Instructor instructorDb = instructorRepository.save(newInstructor);

        Long classroomId;

        if (instructorDb.getClassroom() == null) {
            classroomId = null;
        } else {
            classroomId = instructorDb.getClassroom().getId();
        }

        ResponseInstructorDTO responseInstructor = new ResponseInstructorDTO(
                instructorDb.getId(),
                new ResponseUserDTO(
                        instructorDb.getUser().getId(),
                        instructorDb.getUser().getFirstName(),
                        instructorDb.getUser().getLastName(),
                        instructorDb.getUser().getEmail(),
                        instructorDb.getUser().getLinkedInLink()
                ),
                classroomId
        );
        return responseInstructor;
    }

    public ResponseInstructorDTO getById(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "instructor not found"
                )
        );

        Long classroomId;

        if (instructor.getClassroom() == null) {
            classroomId = null;
        } else {
            classroomId = instructor.getClassroom().getId();
        }

        ResponseInstructorDTO responseInstructor = new ResponseInstructorDTO(
                instructor.getId(),
                new ResponseUserDTO(
                        instructor.getUser().getId(),
                        instructor.getUser().getFirstName(),
                        instructor.getUser().getLastName(),
                        instructor.getUser().getEmail(),
                        instructor.getUser().getLinkedInLink()
                ),
                classroomId
        );
        return responseInstructor;
    }

    public List<ResponseInstructorDTO> getAll() {
        List<Instructor> instructors = instructorRepository.findAll();
        return mapToResponseInstructors(instructors);
    }

    @Transactional
    public ResponseInstructorDTO update(Long id, UpdateInstructorDTO instructorDTO) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "student not found"
                )
        );

        User user = instructor.getUser();

        user.setFirstName(instructorDTO.user().firstName());
        user.setLastName(instructorDTO.user().lastName());
        user.setEmail(instructorDTO.user().email());
        user.setPassword(passwordEncrypt.encoder().encode(instructorDTO.user().password()));
        user.setLinkedInLink(instructorDTO.user().linkedInLink());

        instructor.setUser(user);

        Instructor updatedInstructor = instructorRepository.save(instructor);

        Long classroomId;

        if (instructor.getClassroom() == null) {
            classroomId = null;
        } else {
            classroomId = instructor.getClassroom().getId();
        }

        ResponseInstructorDTO responseInstructor = new ResponseInstructorDTO(
                updatedInstructor.getId(),
                new ResponseUserDTO(
                        updatedInstructor.getUser().getId(),
                        updatedInstructor.getUser().getFirstName(),
                        updatedInstructor.getUser().getLastName(),
                        updatedInstructor.getUser().getEmail(),
                        updatedInstructor.getUser().getLinkedInLink()
                ),
                classroomId
        );
        return responseInstructor;
    }

    @Transactional
    public void deleteById(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "instructor not found"
                )
        );

        instructor.getUser().getRoles().removeAll(instructor.getUser().getRoles());

        instructorRepository.delete(instructor);
    }

    public List<Instructor> getAllInstructorsById(List<Long> instructorsIds) {
        List<Instructor> instructors = instructorRepository.findAllByIdIn(instructorsIds);

        if (instructors.size() != instructorsIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more instructors not found");
        }

        return instructors;
    }


    @Transactional
    public List<ResponseInstructorDTO> attributeInstructorsToClassroom(Classroom classroom, List<Instructor> instructors) {
        List<Long> idList = instructors.stream()
                .map(Instructor::getId).toList();

        List<Instructor> checkedInstructors = instructorRepository.findAllByIdIn(idList);

        if(idList.size() != checkedInstructors.size()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "one or more instructors not exists"
            );
        }

        for (Instructor instructor : instructors) {
            instructor.setClassroom(classroom);
        }
        List<Instructor> updatedInstructors = instructorRepository.saveAll(instructors);
        return mapToResponseInstructors(updatedInstructors);
    }

    public ResponseInstructorDTO mapToResponseInstructor(Instructor instructor) {
        Long classroomId;

        if (instructor.getClassroom() == null) {
            classroomId = null;
        } else {
            classroomId = instructor.getClassroom().getId();
        }

        return new ResponseInstructorDTO(
                instructor.getId(),
                userService.mapToResponseUser(instructor.getUser()),
                classroomId
        );
    }

    public List<ResponseInstructorDTO> mapToResponseInstructors(List<Instructor> instructors) {
        List<ResponseInstructorDTO> instructorsNoPassword = new ArrayList<>();

        for (Instructor instructor : instructors) {
            instructorsNoPassword.add(mapToResponseInstructor(instructor));
        }
        return instructorsNoPassword;
    }
}
