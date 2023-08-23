package uol.compass.cspcapi.domain.scrumMaster;

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
@Table(name = "scrum_masters")
public class ScrumMaster {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "id")
    private Classroom classroom;

    public ScrumMaster(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    public ScrumMaster(User user) {
        this.user = user;
    }
}
