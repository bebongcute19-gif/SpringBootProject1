package re.edu.repository.assessmentRep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.edu.model.entity.AssessmentRound;

import java.util.List;

@Repository
public interface AssessmentRoundRepository
        extends JpaRepository<AssessmentRound, Integer> {

    List<AssessmentRound>
    findByPhase_PhaseId(Integer phaseId);
}