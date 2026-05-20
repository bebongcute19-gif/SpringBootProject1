package re.edu.service;

import re.edu.model.dto.request.internshipPhaseReq.InternshipPhaseRequest;
import re.edu.model.dto.response.internshipPhaseRes.InternshipPhaseResponse;

import java.util.List;

public interface InternshipPhaseService {

    List<InternshipPhaseResponse> getAllPhases();

    InternshipPhaseResponse getPhaseById(Integer phaseId);

    InternshipPhaseResponse createPhase(
            InternshipPhaseRequest request
    );

    InternshipPhaseResponse updatePhase(
            Integer phaseId,
            InternshipPhaseRequest request
    );

    void deletePhase(Integer phaseId);
}