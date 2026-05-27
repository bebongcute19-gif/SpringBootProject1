package re.edu.model.dto.response.assessmentRoundRes;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import re.edu.model.dto.response.roundcriteriares.CreatAssementRound;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "phaseId",
        "phaseName",
        "roundName",
        "startDate",
        "endDate",
        "description",
        "isActive",
        "criteria"
})
public class CreateAssessmnet {

    private Integer id;

    private Integer phaseId;

    private String phaseName;

    private String roundName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private Boolean isActive;

    private List<CreatAssementRound> criteria;
}
