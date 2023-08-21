package uol.compass.cspcapi.application.api.grade.dto;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record UpdateGradeDTO (
        @NotNull(message = "communication must not be null")
        @DecimalMax(value = "10.00", message = "communication can be 10.00 at max")
        @DecimalMin(value = "0.00", message = "communication can be 0.00 at min")
        BigDecimal communication,

        @NotNull(message = "collaboration must not be null")
        @DecimalMax(value = "10.00", message = "collaboration can be 10.00 at max")
        @DecimalMin(value = "0.00", message = "collaboration can be 0.00 at min")
        BigDecimal collaboration,

        @NotNull(message = "autonomy must not be null")
        @DecimalMax(value = "10.00", message = "autonomy can be 10.00 at max")
        @DecimalMin(value = "0.00", message = "autonomy can be 0.00 at min")
        BigDecimal autonomy,

        @NotNull(message = "quiz must not be null")
        @DecimalMax(value = "10.00", message = "quiz can be 10.00 at max")
        @DecimalMin(value = "0.00", message = "quiz can be 0.00 at min")
        BigDecimal quiz,

        @NotNull(message = "individualChallenge must not be null")
        @DecimalMax(value = "10.00", message = "individualChallenge can be 10.00 at max")
        @DecimalMin(value = "0.00", message = "individualChallenge can be 0.00 at min")
        BigDecimal individualChallenge,

        @NotNull(message = "squadChallenge must not be null")
        @DecimalMax(value = "10.00", message = "squadChallenge can be 10.00 at max")
        @DecimalMin(value = "0.00", message = "squadChallenge can be 0.00 at min")
        BigDecimal squadChallenge
) {
        public BigDecimal calculateFinalGrade() {
                BigDecimal sum = communication
                        .add(collaboration)
                        .add(autonomy)
                        .add(quiz)
                        .add(individualChallenge)
                        .add(squadChallenge);

                return sum.divide(BigDecimal.valueOf(6), 2, BigDecimal.ROUND_HALF_UP);
        }

        /*
    public UpdateGradeDTO(BigDecimal communication, BigDecimal collaboration, BigDecimal autonomy, BigDecimal quiz, BigDecimal individualChallenge, BigDecimal squadChallenge) {
        this(
                communication,
                collaboration,
                autonomy,
                quiz,
                individualChallenge,
                squadChallenge,
                // Calculating finalGrade variable
                communication.multiply(new BigDecimal("1"))
                        .add(collaboration.multiply(new BigDecimal("1")))
                        .add(autonomy.multiply(new BigDecimal("1")))
                        .add(quiz.multiply(new BigDecimal("1")))
                        .add(individualChallenge.multiply(new BigDecimal("1")))
                        .add(squadChallenge.multiply(new BigDecimal("1")))
                        .divide(new BigDecimal("6"), RoundingMode.HALF_UP)
        );
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

    public UpdateGradeDTO(Double communication, Double collaboration, Double autonomy, Double quiz, Double individualChallenge, Double squadChallenge) {
        this(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);
        finalGrade = 10.00;
    }

    private Double communication;
    private Double collaboration;
    private Double autonomy;
    private Double quiz;
    private Double individualChallenge;
    private Double squadChallenge;
    private Double finalGrade;

    public UpdateGradeDTO(Double communication, Double collaboration, Double autonomy, Double quiz, Double individualChallenge, Double squadChallenge) {
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

    public Double getCommunication() {
        return communication;
    }

    public Double getCollaboration() {
        return collaboration;
    }

    public Double getAutonomy() {
        return autonomy;
    }

    public Double getQuiz() {
        return quiz;
    }

    public Double getIndividualChallenge() {
        return individualChallenge;
    }

    public Double getSquadChallenge() {
        return squadChallenge;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

     */
}
