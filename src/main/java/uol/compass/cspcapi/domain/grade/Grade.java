package uol.compass.cspcapi.domain.grade;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double communication;
    private Double collaboration;
    private Double autonomy;
    private Double quiz;
    private Double individualChallenge;
    private Double squadChallenge;
    private Double finalGrade;

    public Grade(Double communication, Double collaboration, Double autonomy, Double quiz, Double individualChallenge, Double squadChallenge) {
        this.communication = communication;
        this.collaboration = collaboration;
        this.autonomy = autonomy;
        this.quiz = quiz;
        this.individualChallenge = individualChallenge;
        this.squadChallenge = squadChallenge;
        this.finalGrade = calculateFinalGrade(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);
    }

    public Double calculateFinalGrade(Double communication, Double collaboration, Double autonomy, Double quiz, Double individualChallenge, Double squadChallenge) {
        Double result = ((communication * 1) + (collaboration * 1) + (autonomy * 1) + (quiz * 1) + (individualChallenge * 1) + (squadChallenge * 1)) / 6;
        return result;
    }
}
