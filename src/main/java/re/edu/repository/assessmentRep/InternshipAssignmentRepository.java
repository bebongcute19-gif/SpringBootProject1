package re.edu.repository.assessmentRep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.edu.model.entity.InternshipAssignment;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipAssignmentRepository
        extends JpaRepository<InternshipAssignment, Integer> {

    List<InternshipAssignment>
    findByStudent_Id(Integer studentId);

    List<InternshipAssignment>
    findByMentor_Id(Integer mentorId);

    Optional<InternshipAssignment>
    findByStudent_IdAndPhase_PhaseId(
            Integer studentId,
            Integer phaseId
    );
    boolean existsByStudent_Id(Integer studentId);
}