package re.edu.model.dto.request.assessmentRoundReq;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssessmentRoundRequest {

    private Integer phaseId;

    private String roundName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private Boolean isActive;
}