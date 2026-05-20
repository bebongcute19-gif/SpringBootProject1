package re.edu.repository.internshipRep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.edu.model.entity.EvaluationCriteria;

@Repository
public interface EvaluationCriteriaRepository
        extends JpaRepository<EvaluationCriteria, Integer> {

    boolean existsByCriterionName(String criterionName);
}