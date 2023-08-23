package uol.compass.cspcapi.application.api.grade.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

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
}
