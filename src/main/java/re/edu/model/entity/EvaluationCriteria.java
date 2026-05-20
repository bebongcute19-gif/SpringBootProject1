package re.edu.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "evaluation_criteria")
public class EvaluationCriteria{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer criterionId;
    @Column(nullable = false, unique = true, length = 200)
    private String criterionName;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false,precision = 5, scale = 2)
    private BigDecimal maxScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "criterion")
    private List<RoundCriteria> roundCriteria;

    @OneToMany(mappedBy = "criterion")
    private List<AssessmentResult> assessmentResults;
}
