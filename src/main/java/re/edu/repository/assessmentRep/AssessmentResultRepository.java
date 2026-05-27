package re.edu.repository.assessmentRep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import re.edu.model.entity.AssessmentResult;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentResultRepository
        extends JpaRepository<AssessmentResult, Integer> {
    @Query("""
        SELECT ar
        FROM AssessmentResult ar
        JOIN FETCH ar.assignment a
        JOIN FETCH a.student s
        JOIN FETCH s.user su
        JOIN FETCH a.mentor m
        JOIN FETCH m.user mu
        JOIN FETCH ar.round r
        JOIN FETCH ar.criterion c
        JOIN FETCH ar.evaluatedBy e
    """)
    List<AssessmentResult> findAllWithDetails();
    List<AssessmentResult>
    findByAssignment_AssignmentId(
            Integer assignmentId
    );

    List<AssessmentResult>
    findByAssignment_Mentor_Id(
            Integer mentorId
    );

    List<AssessmentResult>
    findByAssignment_Student_Id(
            Integer studentId
    );

    Optional<AssessmentResult>
    findByAssignment_AssignmentIdAndRound_IdAndCriterion_CriterionId(
            Integer assignmentId,
            Integer roundId,
            Integer criterionId
    );
    boolean existsByRound_Id(Integer roundId);
    List<AssessmentResult> findByEvaluatedBy_Id(Integer userId);
    List<AssessmentResult> findByAssignment_AssignmentIdAndEvaluatedBy_Id(Integer assignmentId, Integer userId);
    List<AssessmentResult> findByAssignment_AssignmentIdAndAssignment_Student_Id(Integer assignmentId, Integer studentId);
}