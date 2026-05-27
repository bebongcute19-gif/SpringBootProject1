package re.edu.model.dto.request.assessmentRoundReq;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateAssessmentRound {
    private Integer phaseId;

    private String roundName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private Boolean isActive;
}
