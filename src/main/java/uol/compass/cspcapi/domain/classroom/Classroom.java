package uol.compass.cspcapi.domain.classroom;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.student.Student;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "classrooms")
public class Classroom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne
    private Coordinator coordinator;

    private BigDecimal progress;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Instructor> instructors;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<ScrumMaster> scrumMasters;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Squad> squads;

    public Classroom(String title, Coordinator coordinator) {
        this.title = title;
        this.coordinator = coordinator;
    }

    public Classroom(String title, Coordinator coordinator, BigDecimal progress) {
        this.title = title;
        this.coordinator = coordinator;
        this.progress = progress;
    }

    public Classroom(List<Student> students, List<Instructor> instructors, List<ScrumMaster> scrumMasters, List<Squad> squads) {
        this.students = students;
        this.instructors = instructors;
        this.scrumMasters = scrumMasters;
        this.squads = squads;
    }
}
