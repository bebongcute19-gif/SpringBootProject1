package re.edu.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "assessment_results",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"assignment_id", "round_id", "criterion_id"})
        }
)
@Getter
@Setter
public class AssessmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private InternshipAssignment assignment;

    @ManyToOne
    @JoinColumn(name = "round_id", nullable = false)
    private AssessmentRound round;

    @ManyToOne
    @JoinColumn(name = "criterion_id", nullable = false)
    private EvaluationCriteria criterion;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "evaluated_by", nullable = false)
    private User evaluatedBy;

    private LocalDateTime evaluationDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
