package re.edu.service;

import re.edu.model.dto.request.assessmentRoundReq.AssessmentRoundRequest;
import re.edu.model.dto.response.assessmentRoundRes.AssessmentRoundResponse;
import re.edu.model.dto.response.assessmentRoundRes.CreateAssessmnet;

import java.util.List;

public interface AssessmentRoundService {

    List<AssessmentRoundResponse> getAllRounds(Integer phaseId);

    AssessmentRoundResponse getRoundById(Integer roundId);

    CreateAssessmnet createRound(AssessmentRoundRequest request);

    AssessmentRoundResponse updateRound(Integer roundId, AssessmentRoundRequest request);

    String deleteRound(
            Integer roundId
    );
}