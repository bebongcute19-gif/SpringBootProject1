    package re.edu.model.dto.response.roundcriteriares;

    import lombok.Getter;
    import lombok.Setter;

    import java.math.BigDecimal;
    @Getter
    @Setter
    public class CreatAssementRound {
        private Integer criterionId;

        private String criterionName;

        private BigDecimal weight;
    }
