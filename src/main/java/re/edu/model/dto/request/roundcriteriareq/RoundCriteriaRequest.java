package re.edu.model.dto.request.roundcriteriareq;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoundCriteriaRequest {

    private Integer roundId;

    private Integer criterionId;

    private BigDecimal weight;
}