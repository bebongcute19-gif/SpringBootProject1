package re.edu.model.dto.response.assessmentresultres;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AssessmentResultResponse {

    private Integer resultId;

    private Integer assignmentId;

    private Integer studentId;

    private String studentName;

    private Integer mentorId;

    private String mentorName;

    private Integer roundId;

    private String roundName;

    private Integer criterionId;

    private String criterionName;

    private BigDecimal score;

    private String comments;

    private Integer evaluatedById;

    private String evaluatedByName;

    private LocalDateTime evaluationDate;
}