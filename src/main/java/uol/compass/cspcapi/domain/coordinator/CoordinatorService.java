package uol.compass.cspcapi.domain.coordinator;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.coordinator.dto.CreateCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.UpdateCoordinatorDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.classroom.ClassroomRepository;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoordinatorService {
    private CoordinatorRepository coordinatorRepository;

    private final UserService userService;
    private final ClassroomRepository classroomRepository;
    private final PasswordEncoder passwordEncrypt;
    private final RoleService roleService;

    @Autowired
    public CoordinatorService(CoordinatorRepository coordinatorRepository, UserService userService, ClassroomRepository classroomRepository, PasswordEncoder passwordEncrypt, RoleService roleService) {
        this.coordinatorRepository = coordinatorRepository;
        this.userService = userService;
        this.classroomRepository = classroomRepository;
        this.passwordEncrypt = passwordEncrypt;
        this.roleService = roleService;
    }

    @Transactional
    public ResponseCoordinatorDTO save(CreateCoordinatorDTO coordinator) {
        Optional<User> alreadyExists = userService.findByEmail(coordinator.user().email());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        User user = new User(
                coordinator.user().firstName(),
                coordinator.user().lastName(),
                coordinator.user().email(),
                passwordEncrypt.encoder().encode(coordinator.user().password()),
                coordinator.user().linkedInLink()
        );
      
        user.getRoles().add(roleService.findRoleByName("ROLE_COORDINATOR"));

        Coordinator newCoordinator = new Coordinator(user);
        Coordinator coordinatorDb = coordinatorRepository.save(newCoordinator);

        ResponseCoordinatorDTO responseCoordinator = new ResponseCoordinatorDTO(
                coordinatorDb.getId(),
                new ResponseUserDTO(
                        coordinatorDb.getUser().getId(),
                        coordinatorDb.getUser().getFirstName(),
                        coordinatorDb.getUser().getLastName(),
                        coordinatorDb.getUser().getEmail(),
                        coordinatorDb.getUser().getLinkedInLink()
                )
        );

        return responseCoordinator;
    }

    public ResponseCoordinatorDTO getById(Long id) {
        return mapToResponseCoordinator(coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        ));
    }

    public Coordinator getByIdOriginal(Long id) {
        return coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );
    }

    public List<ResponseCoordinatorDTO> getAll() {
        List<Coordinator> coordinators = coordinatorRepository.findAll();
        List<ResponseCoordinatorDTO> coordinatorsNoPassword = new ArrayList<>();

        for (Coordinator coordinator : coordinators) {
            coordinatorsNoPassword.add(mapToResponseCoordinator(coordinator));
        }
        return coordinatorsNoPassword;
    }

    @Transactional
    public ResponseCoordinatorDTO update(Long id, UpdateCoordinatorDTO coordinatorDTO) {
        Coordinator coordinator = coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "student not found"
                )
        );

        User user = coordinator.getUser();

        user.setFirstName(coordinatorDTO.user().firstName());
        user.setLastName(coordinatorDTO.user().lastName());
        user.setEmail(coordinatorDTO.user().email());
        user.setPassword(passwordEncrypt.encoder().encode(coordinatorDTO.user().password()));
        user.setLinkedInLink(coordinatorDTO.user().linkedInLink());

        coordinator.setUser(user);

        Coordinator updatedCoordinator = coordinatorRepository.save(coordinator);

        ResponseCoordinatorDTO responseCoordinator = new ResponseCoordinatorDTO(
                updatedCoordinator.getId(),
                new ResponseUserDTO(
                        updatedCoordinator.getUser().getId(),
                        updatedCoordinator.getUser().getFirstName(),
                        updatedCoordinator.getUser().getLastName(),
                        updatedCoordinator.getUser().getEmail(),
                        updatedCoordinator.getUser().getLinkedInLink()
                )
        );

        return responseCoordinator;
    }

    @Transactional
    public void deleteById(Long id) {
        Coordinator coordinator = coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );

        coordinator.getUser().getRoles().removeAll(coordinator.getUser().getRoles());

        Classroom classroom = classroomRepository.findByCoordinatorId(id);
        classroom.setCoordinator(null);

        coordinatorRepository.delete(coordinator);
    }

    public ResponseCoordinatorDTO mapToResponseCoordinator(Coordinator coordinator) {
        ResponseCoordinatorDTO responseCoordinatorDTO;

        if (coordinator != null) {
            responseCoordinatorDTO = new ResponseCoordinatorDTO(
                    coordinator.getId(),
                    userService.mapToResponseUser(coordinator.getUser())
            );
        } else {
            responseCoordinatorDTO = null;
        }

        return responseCoordinatorDTO;
    }
}
