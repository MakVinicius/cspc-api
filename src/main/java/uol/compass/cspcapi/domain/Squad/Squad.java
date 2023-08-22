package uol.compass.cspcapi.domain.Squad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.student.Student;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "squads")
public class Squad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "squad", fetch = FetchType.LAZY)
    private List<Student> students;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "id")
    private Classroom classroom;

    public Squad(String name) {
        this.name = name;
    }

    public Squad(List<Student> students) {
        this.students = students;
    }
}
