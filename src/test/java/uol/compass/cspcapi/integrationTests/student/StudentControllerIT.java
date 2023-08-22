package uol.compass.cspcapi.integrationTests.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uol.compass.cspcapi.application.api.auth.dto.LoginDTO;
import uol.compass.cspcapi.application.api.grade.dto.UpdateGradeDTO;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentsGradeDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.user.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class StudentControllerIT {
    @Autowired
    private MockMvc mockMvc;

    public String login() throws Exception {
        String email = "admin@mail.com";
        String password = "12345678";

        LoginDTO loginDTO = new LoginDTO(email, password);

        MvcResult result = mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String token = JsonPath.read(responseJson, "$.data");

        return token;
    }

    @Disabled
    @Test
    public void testCreateStudent_Success() throws Exception {
        String firstName = "User1";
        String lastName = "Surname";
        String email = "user1surname123@mail.com";
        String password = "password";

        CreateStudentDTO studentDTO = new CreateStudentDTO(
                new CreateUserDTO(firstName, lastName, email, password, null)
        );

        String authToken = login();

        MvcResult result = this.mockMvc.perform(post("/students")
                        .header("Authorization", "Bearer " + authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(studentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.firstName").value(firstName))
                .andExpect(jsonPath("$.user.lastName").value(lastName))
                .andExpect(jsonPath("$.user.email").value(email))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String responseFirstName = JsonPath.read(responseJson, "$.user.firstName");
        String responseLastName = JsonPath.read(responseJson, "$.user.lastName");
        String responseEmail = JsonPath.read(responseJson, "$.user.email");

        assertEquals(firstName, responseFirstName);
        assertEquals(lastName, responseLastName);
        assertEquals(email, responseEmail);
    }

    @Disabled
    @Test
    public void testGetByIdStudent_Success() throws Exception {
        String firstName = "Carolina";
        String lastName = "Koike";
        String email = "carolina.koike.pb@compasso.com.br";
        Long studentId = 4L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/students/" + studentId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.user.firstName").value(firstName))
                .andExpect(jsonPath("$.user.lastName").value(lastName))
                .andExpect(jsonPath("$.user.email").value(email))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Integer responseId = JsonPath.read(responseJson, "$.id");
        Long responseStudentid = responseId.longValue();

        String responseFirstName = JsonPath.read(responseJson, "$.user.firstName");
        String responseLastName = JsonPath.read(responseJson, "$.user.lastName");
        String responseEmail = JsonPath.read(responseJson, "$.user.email");


        assertEquals(studentId, responseStudentid);
        assertEquals(firstName, responseFirstName);
        assertEquals(lastName, responseLastName);
        assertEquals(email, responseEmail);
    }

    @Disabled
    @Test
    public void testGetAllStudents_Success() throws Exception {
        User user_1 = new User("", "", "", "");
        User user_2 = new User("", "", "", "");

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/students")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.email").value(user_1.getEmail()))
                .andExpect(jsonPath("$[1].user.email").value(user_2.getEmail()))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        String responseEmail_1 = JsonPath.read(responseJson, "$[0].user.email");
        String responseEmail_2 = JsonPath.read(responseJson, "$[1].user.email");

        assertEquals(user_1.getEmail(), responseEmail_1);
        assertEquals(user_2.getEmail(), responseEmail_2);
    }

    @Disabled
    @Test
    public void testUpdateStudent_Success() throws Exception {
        Long studentId = 2L;
        UpdateUserDTO updatedUser = new UpdateUserDTO("Update", "User", "updatedemail1@mail.com", "udpatedpassword", null);
        UpdateStudentDTO scrumMasterDTO = new UpdateStudentDTO(updatedUser);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(put("/students/" + studentId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(scrumMasterDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.firstName").value(updatedUser.firstName()))
                .andExpect(jsonPath("$.user.lastName").value(updatedUser.lastName()))
                .andExpect(jsonPath("$.user.email").value(updatedUser.email()))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        String responseFirstName = JsonPath.read(responseJson, "$.user.firstName");
        String responseLastName = JsonPath.read(responseJson, "$.user.lastName");
        String responseEmail = JsonPath.read(responseJson, "$.user.email");


        assertEquals(updatedUser.firstName(), responseFirstName);
        assertEquals(updatedUser.lastName(), responseLastName);
        assertEquals(updatedUser.email(), responseEmail);
    }

    @Disabled
    @Test
    public void testDeleteScrumMaster_Success() throws Exception {
        Long studentId = 2L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(delete("/students/" + studentId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Disabled
    @Test
    public void testUpdateGradesFromStudent_Success() throws Exception {
        BigDecimal communication = new BigDecimal(6.4);
        BigDecimal collaboration = new BigDecimal(6.7);
        BigDecimal autonomy = new BigDecimal(7.5);
        BigDecimal quiz = new BigDecimal(6.8);
        BigDecimal individualChallenge = new BigDecimal(8.7);
        BigDecimal squadChallenge = new BigDecimal(10.00);

        BigDecimal multiplier = new BigDecimal(1);
        BigDecimal finalGrade = communication.multiply(multiplier)
                .add(collaboration.multiply(multiplier))
                .add(autonomy.multiply(multiplier))
                .add(quiz.multiply(multiplier))
                .add(individualChallenge.multiply(multiplier))
                .add(squadChallenge.multiply(multiplier))
                .divide(new BigDecimal("6"), RoundingMode.HALF_UP);

        UpdateGradeDTO grades = new UpdateGradeDTO(
                communication,
                collaboration,
                autonomy,
                quiz,
                individualChallenge,
                squadChallenge
        );

        Long studentId = 1L;
        UpdateStudentsGradeDTO studentDTO = new UpdateStudentsGradeDTO(grades);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/students/" + studentId + "/update-grades")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grades.communication").value(communication))
                .andExpect(jsonPath("$.grades.collaboration").value(collaboration))
                .andExpect(jsonPath("$.grades.autonomy").value(autonomy))
                .andExpect(jsonPath("$.grades.quiz").value(quiz))
                .andExpect(jsonPath("$.grades.individualChallenge").value(individualChallenge))
                .andExpect(jsonPath("$.grades.squadChallenge").value(squadChallenge))
                .andExpect(jsonPath("$.grades.finalGrade").value(finalGrade))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Double studentGradeCommunication = JsonPath.read(responseJson, "$.grades.communication");
        Double studentGradeCollaboration = JsonPath.read(responseJson, "$.grades.collaboration");
        Double studentGradeAutonomy = JsonPath.read(responseJson, "$.grades.autonomy");
        Double studentGradeQuiz = JsonPath.read(responseJson, "$.grades.quiz");
        Double studentGradeIndividualChallenge = JsonPath.read(responseJson, "$.grades.individualChallenge");
        Double studentGradeSquadChallenge = JsonPath.read(responseJson, "$.grades.squadChallenge");
        Double studentGradeFinalGrade = JsonPath.read(responseJson, "$.grades.finalGrade");

        assertEquals(communication, studentGradeCommunication);
        assertEquals(collaboration, studentGradeCollaboration);
        assertEquals(autonomy, studentGradeAutonomy);
        assertEquals(quiz, studentGradeQuiz);
        assertEquals(individualChallenge, studentGradeIndividualChallenge);
        assertEquals(squadChallenge, studentGradeSquadChallenge);
        assertEquals(finalGrade, studentGradeFinalGrade);
    }


    // Helper method to convert object to JSON string
    private static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
