package uol.compass.cspcapi.domain.scrumMaster;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.UpdateScrumMasterDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
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
public class ScrumMasterService {

    private final ScrumMasterRepository scrumMasterRepository;

    private final UserService userService;
    private final PasswordEncoder passwordEncrypt;
    private final RoleService roleService;

    @Autowired
    public ScrumMasterService(ScrumMasterRepository scrumMasterRepository, UserService userService, PasswordEncoder passwordEncrypt, RoleService roleService) {
        this.scrumMasterRepository = scrumMasterRepository;
        this.userService = userService;
        this.passwordEncrypt = passwordEncrypt;
        this.roleService = roleService;
    }

    @Transactional
    public ResponseScrumMasterDTO save(CreateScrumMasterDTO scrumMaster) {
        Optional<User> alreadyExists = userService.findByEmail(scrumMaster.user().email());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        User user = new User(
                scrumMaster.user().firstName(),
                scrumMaster.user().lastName(),
                scrumMaster.user().email(),
                passwordEncrypt.encoder().encode(scrumMaster.user().password())
        );

        user.getRoles().add(roleService.findRoleByName("ROLE_SCRUM_MASTER"));
        ScrumMaster newScrumMaster = new ScrumMaster(user);
        ScrumMaster scrumMasterDb = scrumMasterRepository.save(newScrumMaster);
        
        return mapToResponseScrumMaster(scrumMasterDb);
    }

    public ResponseScrumMasterDTO getById(Long id){
        return mapToResponseScrumMaster(scrumMasterRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "scrum master not found"
                )
        ));
    }

    public List<ResponseScrumMasterDTO> getAll(){
        List<ScrumMaster> scrumMasters = scrumMasterRepository.findAll();
//        List<ResponseScrumMasterDTO> scrumMastersNoPassword = new ArrayList<>();
//
//        for (ScrumMaster scrumMaster : scrumMasters) {
//            scrumMastersNoPassword.add(mapToResponseScrumMaster(scrumMaster));
//        }
//        return scrumMastersNoPassword;
        return mapToResponseScrumMasters(scrumMasters);
    }

    public ResponseScrumMasterDTO update(Long id, UpdateScrumMasterDTO scrumMasterDTO) {
        ScrumMaster scrumMaster = scrumMasterRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "scrummaster not found"
                )
        );

        User user = scrumMaster.getUser();

        user.setFirstName(scrumMasterDTO.user().firstName());
        user.setLastName(scrumMasterDTO.user().lastName());
        user.setEmail(scrumMasterDTO.user().email());
        user.setPassword(passwordEncrypt.encoder().encode(scrumMasterDTO.user().password()));

        scrumMaster.setUser(user);

        ScrumMaster updatedScrumMasters = scrumMasterRepository.save(scrumMaster);

        return mapToResponseScrumMaster(updatedScrumMasters);
    }

    @Transactional
    public void delete(Long id){
        ScrumMaster scrumMaster = scrumMasterRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "scrum master not found"
                )
        );

        scrumMaster.getUser().getRoles().removeAll(scrumMaster.getUser().getRoles());

        scrumMasterRepository.delete(scrumMaster);
    }

    public List<ScrumMaster> getAllScrumMastersById(List<Long> scrumMastersIds) {


        List<ScrumMaster> scrumMasters = scrumMasterRepository.findAllByIdIn(scrumMastersIds);

        if(scrumMasters.size() != scrumMastersIds.size()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "one or more scrum masters not exists"
            );
        }

        return scrumMasters;
    }

    @Transactional
    public List<ResponseScrumMasterDTO> attributeScrumMastersToClassroom(Classroom classroom, List<ScrumMaster> scrumMasters) {
        for (ScrumMaster scrumMaster : scrumMasters) {
            scrumMaster.setClassroom(classroom);
        }
        List<ScrumMaster> updatedScrumMasters = scrumMasterRepository.saveAll(scrumMasters);
//        List<ResponseScrumMasterDTO> scrumMastersNoPassword = new ArrayList<>();
//
//        for (ScrumMaster scrumMaster : scrumMasters) {
//            scrumMastersNoPassword.add(mapToResponseScrumMaster(scrumMaster));
//        }
//
//        return scrumMastersNoPassword;
        return mapToResponseScrumMasters(updatedScrumMasters);
    }

    public ResponseScrumMasterDTO mapToResponseScrumMaster(ScrumMaster scrumMaster) {
        Long classroomId;

        if (scrumMaster.getClassroom() == null) {
            classroomId = null;
        } else {
            classroomId = scrumMaster.getClassroom().getId();
        }

        return new ResponseScrumMasterDTO(
                scrumMaster.getId(),
                userService.mapToResponseUser(scrumMaster.getUser()),
                classroomId
        );
    }

    public List<ResponseScrumMasterDTO> mapToResponseScrumMasters(List<ScrumMaster> scrumMasters) {
        List<ResponseScrumMasterDTO> scrumMastersNoPassword = new ArrayList<>();

        for (ScrumMaster scrumMaster : scrumMasters) {
            scrumMastersNoPassword.add(mapToResponseScrumMaster(scrumMaster));
        }

        return scrumMastersNoPassword;
    }
}
