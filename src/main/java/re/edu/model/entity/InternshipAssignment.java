package re.edu.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import re.edu.model.enums.AssignmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "internship_assignments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "phase_id"})
        }
)
@Getter
@Setter
public class InternshipAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "phase_id", nullable = false)
    private InternshipPhase phase;

    private LocalDateTime assignedDate;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "assignment")
    private List<AssessmentResult> assessmentResults;
}
