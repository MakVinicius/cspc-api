package uol.compass.cspcapi.application.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO (
        @NotBlank(message = "first name must not be empty")
        @Size(min = 3, message = "first name must be at least 3 characters long")
        String firstName,

        @NotBlank(message = "last name must not be empty")
        @Size(min = 3, message = "last name must be at least 3 characters long")
        String lastName,

        @NotBlank(message = "email must not be empty")
        @Email(message = "field must be an email pattern")
        String email,

        @NotBlank(message = "password must not be empty")
        @Size(min = 8, message = "password length must be at least 8 characters long")
        String password,

        String linkedInLink
) {
}
