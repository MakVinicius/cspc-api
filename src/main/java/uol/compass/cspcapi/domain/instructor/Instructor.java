package uol.compass.cspcapi.domain.instructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "instructors")
public class Instructor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "id")
    private Classroom classroom;

    public Instructor(User user) {
        this.user = user;
    }
}
