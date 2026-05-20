package re.edu.model.dto.response.evaluationCriteriaRes;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EvaluationCriteriaResponse {

    private Integer id;

    private String criterionName;

    private String description;

    private BigDecimal maxScore;
}