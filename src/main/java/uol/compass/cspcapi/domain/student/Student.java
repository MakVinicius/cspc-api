package uol.compass.cspcapi.domain.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.grade.Grade;
import uol.compass.cspcapi.domain.user.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Grade grades;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "squad_id", referencedColumnName = "id")
    private Squad squad;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "id")
    private Classroom classroom;

    public Student(User user) {
        this.user = user;
    }

    public Student(Grade grades) {
        this.grades = grades;
    }
}
