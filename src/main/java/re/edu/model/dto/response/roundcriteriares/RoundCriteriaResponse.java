package re.edu.model.dto.response.roundcriteriares;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoundCriteriaResponse {

    private Integer roundCriterionId;

    private Integer roundId;

    private String roundName;

    private Integer criterionId;

    private String criterionName;

    private BigDecimal weight;

    private BigDecimal maxScore;
}