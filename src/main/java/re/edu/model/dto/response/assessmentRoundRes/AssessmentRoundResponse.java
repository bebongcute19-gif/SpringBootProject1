package re.edu.model.dto.response.assessmentRoundRes;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssessmentRoundResponse {

    private Integer id;

    private Integer phaseId;

    private String phaseName;

    private String roundName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private Boolean isActive;
}