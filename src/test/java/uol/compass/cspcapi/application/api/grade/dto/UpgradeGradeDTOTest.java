package uol.compass.cspcapi.application.api.grade.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uol.compass.cspcapi.domain.grade.Grade;

import java.math.BigDecimal;

public class UpgradeGradeDTOTest {

    @Test
    public void testCalculateFinalGrade() {
        // Test the calculateFinalGrade method with some sample values
        BigDecimal communication = new BigDecimal(8.0);
        BigDecimal collaboration = new BigDecimal(9.0);
        BigDecimal autonomy = new BigDecimal(7.5);
        BigDecimal quiz = new BigDecimal(6.0);
        BigDecimal individualChallenge = new BigDecimal(8.5);
        BigDecimal squadChallenge = new BigDecimal(7.0);

        BigDecimal expectedFinalGrade = communication
                .add(collaboration)
                .add(autonomy)
                .add(quiz)
                .add(individualChallenge)
                .add(squadChallenge)
                .divide(BigDecimal.valueOf(6), 2, BigDecimal.ROUND_HALF_UP);

        UpdateGradeDTO updateGradeDTO = new UpdateGradeDTO(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);

        BigDecimal calculatedFinalGrade = updateGradeDTO.calculateFinalGrade();

        // Assert that the calculated final grade matches the expected final grade
        Assertions.assertEquals(expectedFinalGrade, calculatedFinalGrade);
    }

    @Test
    public void testGetters() {
        // Test the getter methods for each field in the class
        BigDecimal communication = new BigDecimal(8.0);
        BigDecimal collaboration = new BigDecimal(9.0);
        BigDecimal autonomy = new BigDecimal(7.5);
        BigDecimal quiz = new BigDecimal(6.0);
        BigDecimal individualChallenge = new BigDecimal(8.5);
        BigDecimal squadChallenge = new BigDecimal(7.0);

        Grade grades = new Grade(
                communication,
                collaboration,
                autonomy,
                quiz,
                individualChallenge,
                squadChallenge
        );
        BigDecimal finalGrade = grades.calculateFinalGrade(
                communication,
                collaboration,
                autonomy,
                quiz,
                individualChallenge,
                squadChallenge
        );

        UpdateGradeDTO updateGradeDTO = new UpdateGradeDTO(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);

        // Assert that the getter methods return the expected values
        Assertions.assertEquals(communication, updateGradeDTO.communication());
        Assertions.assertEquals(collaboration, updateGradeDTO.collaboration());
        Assertions.assertEquals(autonomy, updateGradeDTO.autonomy());
        Assertions.assertEquals(quiz, updateGradeDTO.quiz());
        Assertions.assertEquals(individualChallenge, updateGradeDTO.individualChallenge());
        Assertions.assertEquals(squadChallenge, updateGradeDTO.squadChallenge());
    }
}

