package uol.compass.cspcapi.domain.grade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import uol.compass.cspcapi.domain.student.Student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;


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

    public Grade() {
    }

    public Grade(BigDecimal communication, BigDecimal collaboration, BigDecimal autonomy, BigDecimal quiz, BigDecimal individualChallenge, BigDecimal squadChallenge) {
        this.communication = communication;
        this.collaboration = collaboration;
        this.autonomy = autonomy;
        this.quiz = quiz;
        this.individualChallenge = individualChallenge;
        this.squadChallenge = squadChallenge;
        this.finalGrade = calculateFinalGrade(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCommunication() {
        return communication;
    }

    public void setCommunication(BigDecimal communication) {
        this.communication = communication;
    }

    public BigDecimal getCollaboration() {
        return collaboration;
    }

    public void setCollaboration(BigDecimal collaboration) {
        this.collaboration = collaboration;
    }

    public BigDecimal getAutonomy() {
        return autonomy;
    }

    public void setAutonomy(BigDecimal autonomy) {
        this.autonomy = autonomy;
    }

    public BigDecimal getQuiz() {
        return quiz;
    }

    public void setQuiz(BigDecimal quiz) {
        this.quiz = quiz;
    }

    public BigDecimal getIndividualChallenge() {
        return individualChallenge;
    }

    public void setIndividualChallenge(BigDecimal individualChallenge) {
        this.individualChallenge = individualChallenge;
    }

    public BigDecimal getSquadChallenge() {
        return squadChallenge;
    }

    public void setSquadChallenge(BigDecimal squadChallenge) {
        this.squadChallenge = squadChallenge;
    }

    public BigDecimal getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(BigDecimal finalGrade) {
        this.finalGrade = finalGrade;
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
