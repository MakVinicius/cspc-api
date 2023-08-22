package uol.compass.cspcapi.domain.grade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GradeTest {

    private Grade grade;

    @BeforeEach
    public void setUp() {
        grade = new Grade(
                new BigDecimal(8.5),
                new BigDecimal(7.0),
                new BigDecimal(9.0),
                new BigDecimal(6.5),
                new BigDecimal(8.0),
                new BigDecimal(7.5)
        );
    }

    @Test
    public void testCalculateFinalGrade() {
        BigDecimal multiplier = new BigDecimal(1);
        BigDecimal communication = new BigDecimal(8.5);
        BigDecimal collaboration = new BigDecimal(7.0);
        BigDecimal autonomy = new BigDecimal(9.0);
        BigDecimal quiz = new BigDecimal(6.5);
        BigDecimal individualChallenge = new BigDecimal(8.0);
        BigDecimal squadChallenge = new BigDecimal(7.5);

        BigDecimal expectedFinalGrade = communication.multiply(multiplier)
                .add(collaboration.multiply(multiplier))
                .add(autonomy.multiply(multiplier))
                .add(quiz.multiply(multiplier))
                .add(individualChallenge.multiply(multiplier))
                .add(squadChallenge.multiply(multiplier))
                .divide(new BigDecimal("6"), RoundingMode.HALF_UP);

        BigDecimal actualFinalGrade = grade.calculateFinalGrade(
                grade.getCommunication(),
                grade.getCollaboration(),
                grade.getAutonomy(),
                grade.getQuiz(),
                grade.getIndividualChallenge(),
                grade.getSquadChallenge()
        );

        assertEquals(expectedFinalGrade, actualFinalGrade);
    }

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        BigDecimal communication = new BigDecimal(9.0);
        BigDecimal collaboration = new BigDecimal(8.0);
        BigDecimal autonomy = new BigDecimal(7.5);
        BigDecimal quiz = new BigDecimal(6.0);
        BigDecimal individualChallenge = new BigDecimal(9.5);
        BigDecimal squadChallenge = new BigDecimal(8.5);
        BigDecimal finalGrade = grade.calculateFinalGrade(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);

        grade.setCommunication(communication);
        grade.setCollaboration(collaboration);
        grade.setAutonomy(autonomy);
        grade.setQuiz(quiz);
        grade.setIndividualChallenge(individualChallenge);
        grade.setSquadChallenge(squadChallenge);
        grade.setFinalGrade(finalGrade);
        grade.setId(id);

        assertEquals(id, grade.getId());
        assertEquals(communication, grade.getCommunication());
        assertEquals(collaboration, grade.getCollaboration());
        assertEquals(autonomy, grade.getAutonomy());
        assertEquals(quiz, grade.getQuiz());
        assertEquals(individualChallenge, grade.getIndividualChallenge());
        assertEquals(squadChallenge, grade.getSquadChallenge());

        assertEquals(finalGrade, grade.getFinalGrade());
    }
}
