package uol.compass.cspcapi.commons;

import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.grade.Grade;

public class StudentsConstants {

    public static final ResponseUserDTO RESPONSE_USER_1 = new ResponseUserDTO(1L, "User", "Third", "user.third@mail.com", "linkedInLink");
    public static final ResponseStudentDTO RESPONSE_STUDENT_1 = new ResponseStudentDTO(1L, RESPONSE_USER_1, new Grade(), null, null);

    public static final ResponseUserDTO RESPONSE_USER_2 = new ResponseUserDTO(2L, "User", "Fourth", "user.fourth@mail.com", "linkedInLink");
    public static final ResponseStudentDTO RESPONSE_STUDENT_2 = new ResponseStudentDTO(2L, RESPONSE_USER_2, new Grade(), null, null);

    public static final ResponseUserDTO RESPONSE_USER_3 = new ResponseUserDTO(3L, "User", "Student", "user.student@mail.com", "linkedInLink");
    public static final ResponseStudentDTO RESPONSE_STUDENT_3 = new ResponseStudentDTO(3L, RESPONSE_USER_3, new Grade(), null, null);
}
