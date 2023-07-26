package uol.compass.cspcapi.domain.classroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classrooms, Long> {

    Optional<Classrooms> findById(Long id);
    List<Classrooms> findAll();

    void deleteById(Long id);

    List<Classrooms> findAllBySomeAttribute(String attribute);

    void deleteAll();


}
