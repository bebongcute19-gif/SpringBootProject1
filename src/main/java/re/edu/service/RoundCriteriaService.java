package re.edu.service;

import re.edu.model.dto.request.roundcriteriareq.RoundCriteriaRequest;
import re.edu.model.dto.response.roundcriteriares.RoundCriteriaResponse;

import java.util.List;

public interface RoundCriteriaService {

    List<RoundCriteriaResponse> getAllRoundCriteria(Integer roundId);

    RoundCriteriaResponse getRoundCriteriaById(Integer id);

    RoundCriteriaResponse createRoundCriteria(RoundCriteriaRequest request);

    RoundCriteriaResponse updateRoundCriteria(Integer id, RoundCriteriaRequest request);

    void deleteRoundCriteria(Integer id);
}