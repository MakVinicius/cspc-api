package uol.compass.cspcapi.domain.grade;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal communication;
    private BigDecimal collaboration;
    private BigDecimal autonomy;
    private BigDecimal quiz;
    private BigDecimal individualChallenge;
    private BigDecimal squadChallenge;
    private BigDecimal finalGrade;

    public Grade(BigDecimal communication, BigDecimal collaboration, BigDecimal autonomy, BigDecimal quiz, BigDecimal individualChallenge, BigDecimal squadChallenge) {
        this.communication = communication;
        this.collaboration = collaboration;
        this.autonomy = autonomy;
        this.quiz = quiz;
        this.individualChallenge = individualChallenge;
        this.squadChallenge = squadChallenge;
        this.finalGrade = calculateFinalGrade(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);
    }

    public BigDecimal calculateFinalGrade(BigDecimal communication, BigDecimal collaboration, BigDecimal autonomy, BigDecimal quiz, BigDecimal individualChallenge, BigDecimal squadChallenge) {
        BigDecimal multiplier = new BigDecimal("1");

        BigDecimal result = communication.multiply(multiplier)
                .add(collaboration.multiply(multiplier))
                .add(autonomy.multiply(multiplier))
                .add(quiz.multiply(multiplier))
                .add(individualChallenge.multiply(multiplier))
                .add(squadChallenge.multiply(multiplier))
                .divide(new BigDecimal("6"), RoundingMode.HALF_UP);

        return result;
    }
}
