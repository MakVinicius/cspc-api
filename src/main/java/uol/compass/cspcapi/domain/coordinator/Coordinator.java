package uol.compass.cspcapi.domain.coordinator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.cspcapi.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "coordinators")
public class Coordinator {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public Coordinator(User user) {
        this.user = user;
    }
}
