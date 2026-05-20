package re.edu.service;

import re.edu.model.dto.request.assessmentresultreq.AssessmentResultRequest;
import re.edu.model.dto.request.assessmentresultreq.AssessmentResultUpdateRequest;
import re.edu.model.dto.response.assessmentresultres.AssessmentResultResponse;

import java.util.List;

public interface AssessmentResultService {

    // 42. GET ALL
    List<AssessmentResultResponse> getAllResults(Integer assignmentId);

    // 43. CREATE
    AssessmentResultResponse createResult(AssessmentResultRequest request);

    // 44. UPDATE
    AssessmentResultResponse updateResult(Integer resultId, AssessmentResultUpdateRequest request);
}