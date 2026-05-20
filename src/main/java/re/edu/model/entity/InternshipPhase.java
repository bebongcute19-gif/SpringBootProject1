package re.edu.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="internship_phase")
public class InternshipPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer phaseId;
    @Column(nullable = false,unique = true,length = 100)
    private String phaseName;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "phase")
    private List<AssessmentRound> rounds;

    @OneToMany(mappedBy = "phase")
    private List<InternshipAssignment> assignments;
}
