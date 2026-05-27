package re.edu.repository.assessmentRep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.edu.model.entity.InternshipAssignment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import re.edu.model.entity.Mentor;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipAssignmentRepository
        extends JpaRepository<InternshipAssignment, Integer> {
    boolean existsByMentor_Id(Integer mentorId);
    List<InternshipAssignment>
    findByStudent_Id(Integer studentId);

    List<InternshipAssignment>
    findByMentor_Id(Integer mentorId);

    Optional<InternshipAssignment>
    findByStudent_IdAndPhase_PhaseId(
            Integer studentId,
            Integer phaseId
    );
    @Query("""
       SELECT DISTINCT ia.mentor
       FROM InternshipAssignment ia
       WHERE ia.student.id = :studentId
       """)
    List<Mentor> findDistinctMentorsByStudent_Id(
            Integer studentId
    );
    boolean existsByStudent_IdAndMentor_Id(
            Integer studentId,
            Integer mentorId
    );

    boolean existsByStudent_Id(Integer studentId);
}