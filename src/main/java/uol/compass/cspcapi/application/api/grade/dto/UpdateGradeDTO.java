package uol.compass.cspcapi.application.api.grade.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateGradeDTO (
        @NotNull(message = "communication must not be null")
        Double communication,

        @NotNull(message = "collaboration must not be null")
        Double collaboration,

        @NotNull(message = "autonomy must not be null")
        Double autonomy,

        @NotNull(message = "quiz must not be null")
        Double quiz,

        @NotNull(message = "individualChallenge must not be null")
        Double individualChallenge,

        @NotNull(message = "squadChallenge must not be null")
        Double squadChallenge
) {
        public Double calculateFinalGrade() {
                return ((communication * 1) + (collaboration * 1) + (autonomy * 1) + (quiz * 1) + (individualChallenge * 1) + (squadChallenge * 1) ) / 6;
        }
}
