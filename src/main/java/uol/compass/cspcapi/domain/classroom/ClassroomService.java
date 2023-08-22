package uol.compass.cspcapi.domain.classroom;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomElementsDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {

    private ClassroomRepository classroomRepository;

    private CoordinatorService coordinatorService;
    private StudentService studentService;
    private ScrumMasterService scrumMasterService;
    private InstructorService instructorService;
    private SquadService squadService;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository, CoordinatorService coordinatorService, StudentService studentService, ScrumMasterService scrumMasterService, InstructorService instructorService, SquadService squadService) {
        this.classroomRepository = classroomRepository;
        this.coordinatorService = coordinatorService;
        this.studentService = studentService;
        this.scrumMasterService = scrumMasterService;
        this.instructorService = instructorService;
        this.squadService = squadService;
    }

    @Transactional
    public ResponseClassroomDTO saveClassroom(CreateClassroomDTO classroomDTO, Long coordinatorId) {
        Optional<Classroom> alreadyExists = classroomRepository.findByTitle(classroomDTO.title());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Title already exists"
            );
        }

        Coordinator coordinator = coordinatorService.getByIdOriginal(coordinatorId);

        Classroom classroom = new Classroom(
                classroomDTO.title(),
                coordinator,
                classroomDTO.progress()
        );

        Classroom savedClassroom = classroomRepository.save(classroom);
        return mapToResponseClassroom(savedClassroom);
    }

    public ResponseClassroomDTO getById(Long id){
        return mapToResponseClassroom(classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Classroom not found"
                )
        ));
    }

    public List<ResponseClassroomDTO> getAllClassrooms() {
        List<Classroom> classrooms = classroomRepository.findAll();
        List<ResponseClassroomDTO> classroomsNoPassword = new ArrayList<>();

        for (Classroom classroom : classrooms) {
            classroomsNoPassword.add(mapToResponseClassroom(classroom));
        }

        return classroomsNoPassword;
    }

    @Transactional
    public ResponseClassroomDTO updateClassroom(Long id, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        //classrooms.setTitle(classrooms.getTitle());
        classroom.setTitle(classroomDTO.title());
        classroom.setCoordinator(coordinatorService.getByIdOriginal(classroomDTO.coordinatorId()));
        classroom.setProgress(classroomDTO.progress());

        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public void deleteClassroom(Long classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Squad not found"
                )
        );

        List<Student> toRemoveStudents = classroom.getStudents();
        studentService.attributeStudentsToClassroom(null, toRemoveStudents);

        List<ScrumMaster> toRemoveScrumMasters = classroom.getScrumMasters();
        scrumMasterService.attributeScrumMastersToClassroom(null, toRemoveScrumMasters);

        List<Instructor> toRemoveInstructors = classroom.getInstructors();
        instructorService.attributeInstructorsToClassroom(null, toRemoveInstructors);

        List<Squad> toRemoveSquads = classroom.getSquads();
        squadService.attributeSquadsToClassroom(null, toRemoveSquads);

        toRemoveStudents.removeIf(student -> true);
        classroom.setStudents(toRemoveStudents);

        toRemoveScrumMasters.removeIf(scrumMaster -> true);
        classroom.setScrumMasters(toRemoveScrumMasters);

        toRemoveInstructors.removeIf(instructor -> true);
        classroom.setInstructors(toRemoveInstructors);

        toRemoveSquads.removeIf(squad -> true);
        classroom.setSquads(toRemoveSquads);

        classroomRepository.save(classroom);
        classroomRepository.deleteById(classroom.getId());
    }

    //Jogando users dentro da minha classroom
    @Transactional
    public ResponseClassroomDTO addStudentsToClassroom(Long classroomId, UpdateClassroomElementsDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "classroom not found"));

        List<Student> students = classroom.getStudents();
        List<Student> newStudents = studentService.getAllStudentsById(classroomDTO.generalUsersIds());
        students.addAll(newStudents);

        studentService.attributeStudentsToClassroom(classroom, students);
        classroom.setStudents(students);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO addScrumMastersToClassroom(Long classroomId, UpdateClassroomElementsDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<ScrumMaster> scrumMasters = classroom.getScrumMasters();
        List<ScrumMaster> newScrumMasters = scrumMasterService.getAllScrumMastersById(classroomDTO.generalUsersIds());
        scrumMasters.addAll(newScrumMasters);

        scrumMasterService.attributeScrumMastersToClassroom(classroom, scrumMasters);
        classroom.setScrumMasters(scrumMasters);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO addInstructorsToClassroom(Long classroomId, UpdateClassroomElementsDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Instructor> instructors = classroom.getInstructors();
        List<Instructor> newInstructors = instructorService.getAllInstructorsById(classroomDTO.generalUsersIds());
        instructors.addAll(newInstructors);

        instructorService.attributeInstructorsToClassroom(classroom, instructors);
        classroom.setInstructors(instructors);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO addSquadsToClassroom(Long classroomId, UpdateClassroomElementsDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Squad> squads = classroom.getSquads();
        List<Squad> newSquads = squadService.getAllSquadsById(classroomDTO.generalUsersIds());
        squads.addAll(newSquads);

        squadService.attributeSquadsToClassroom(classroom, squads);
        classroom.setSquads(squads);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }


    @Transactional
    public ResponseClassroomDTO removeStudentsFromClassroom(Long classroomId, UpdateClassroomElementsDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        //classroom.getStudents().removeIf(student -> student.getId().equals(studentId));

        List<Student> students = classroom.getStudents();

        students.removeIf(
                student -> classroomDTO.generalUsersIds().contains(student.getId())
        );

        List<Student> toRemoveStudents = studentService.getAllStudentsById(classroomDTO.generalUsersIds());
        studentService.attributeStudentsToClassroom(null, toRemoveStudents);

        classroom.setStudents(students);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO removeScrumMastersFromClassroom(Long classroomId, UpdateClassroomElementsDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<ScrumMaster> scrumMasters = classroom.getScrumMasters();

        scrumMasters.removeIf(
                scrumMaster -> classroomDTO.generalUsersIds().contains(scrumMaster.getId())
        );

        List<ScrumMaster> toRemoveScrumMasters = scrumMasterService.getAllScrumMastersById(classroomDTO.generalUsersIds());
        scrumMasterService.attributeScrumMastersToClassroom(null, toRemoveScrumMasters);

        classroom.setScrumMasters(scrumMasters);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO removeInstructorsFromClassroom(Long classroomId, UpdateClassroomElementsDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Instructor> instructors = classroom.getInstructors();

        instructors.removeIf(
                instructor -> classroomDTO.generalUsersIds().contains(instructor.getId())
        );

        List<Instructor> toRemoveInstructors = instructorService.getAllInstructorsById(classroomDTO.generalUsersIds());
        instructorService.attributeInstructorsToClassroom(null, toRemoveInstructors);

        classroom.setInstructors(instructors);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO removeSquadsFromClassroom(Long classroomId, UpdateClassroomElementsDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Squad> squads = classroom.getSquads();

        squads.removeIf(
                squad -> classroomDTO.generalUsersIds().contains(squad.getId())
        );

        List<Squad> toRemoveSquads = squadService.getAllSquadsById(classroomDTO.generalUsersIds());
        squadService.attributeSquadsToClassroom(null, toRemoveSquads);

        classroom.setSquads(squads);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    public ResponseClassroomDTO mapToResponseClassroom(Classroom classroom) {
        if (classroom.getStudents() == null) {
            classroom.setStudents(new ArrayList<>());
        }
        if (classroom.getInstructors() == null) {
            classroom.setInstructors(new ArrayList<>());
        }
        if (classroom.getScrumMasters() == null) {
            classroom.setScrumMasters(new ArrayList<>());
        }
        if (classroom.getSquads() == null) {
            classroom.setSquads(new ArrayList<>());
        }
        return new ResponseClassroomDTO(
                classroom.getId(),
                classroom.getTitle(),
                coordinatorService.mapToResponseCoordinator(classroom.getCoordinator()),
                classroom.getProgress(),
                studentService.mapToResponseStudents(classroom.getStudents()),
                instructorService.mapToResponseInstructors(classroom.getInstructors()),
                scrumMasterService.mapToResponseScrumMasters(classroom.getScrumMasters()),
                squadService.mapToResponseSquads(classroom.getSquads())
        );
    }
}


