package re.edu.service;

import re.edu.model.dto.request.evaluationCriteriaReq.EvaluationCriteriaRequest;
import re.edu.model.dto.response.evaluationCriteriaRes.EvaluationCriteriaResponse;

import java.util.List;

public interface EvaluationCriteriaService {

    List<EvaluationCriteriaResponse> getAllCriteria();

    EvaluationCriteriaResponse getCriterionById(Integer criterionId);

    EvaluationCriteriaResponse createCriterion(EvaluationCriteriaRequest request);

    EvaluationCriteriaResponse updateCriterion(Integer criterionId, EvaluationCriteriaRequest request);

    void deleteCriterion(Integer criterionId);
}