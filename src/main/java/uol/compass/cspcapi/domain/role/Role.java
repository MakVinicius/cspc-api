package uol.compass.cspcapi.domain.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

//    @ManyToMany(mappedBy = "roles")
//    private List<User> users = new ArrayList<>();

    private String name;

    public Role(String name) {
        this.name = name;
    }
}
