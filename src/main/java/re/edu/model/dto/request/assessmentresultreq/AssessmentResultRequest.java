package re.edu.model.dto.request.assessmentresultreq;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AssessmentResultRequest {

    @NotNull(message = "Assignment id is required")
    private Integer assignmentId;

    @NotNull(message = "Round id is required")
    private Integer roundId;

    @NotNull(message = "Criterion id is required")
    private Integer criterionId;

    @NotNull(message = "Score is required")
    @DecimalMin(
            value = "0.0",
            message = "Score must be >= 0"
    )
    @DecimalMax(
            value = "10.0",
            message = "Score must be <= 10"
    )
    private BigDecimal score;

    private String comments;
}