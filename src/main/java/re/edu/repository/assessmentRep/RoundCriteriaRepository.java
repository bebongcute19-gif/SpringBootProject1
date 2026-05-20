package re.edu.repository.assessmentRep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.edu.model.entity.RoundCriteria;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundCriteriaRepository
        extends JpaRepository<RoundCriteria, Integer> {

    List<RoundCriteria>
    findByRound_Id(Integer roundId);

    Optional<RoundCriteria>
    findByRound_IdAndCriterion_CriterionId(
            Integer roundId,
            Integer criterionId
    );
}