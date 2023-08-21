package uol.compass.cspcapi.application.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO (
        @NotBlank(message = "username must not be empty")
        @Email(message = "field must be an email pattern")
        String email,

        @NotBlank(message = "password must not be empty")
        @Size(min = 8, message = "password length must be at least 8 characters long")
        String password
) {
//    @NotBlank(message = "username must not be empty")
//    private String email;
//    @NotBlank(message = "password must not be empty")
//    private String password;
//
//    public LoginDTO(String email, String password) {
//        this.email = email;
//        this.password = password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
}
