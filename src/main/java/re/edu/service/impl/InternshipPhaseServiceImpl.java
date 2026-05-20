package re.edu.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.internshipPhaseReq.InternshipPhaseRequest;
import re.edu.model.dto.response.internshipPhaseRes.InternshipPhaseResponse;
import re.edu.model.entity.InternshipPhase;
import re.edu.repository.internshipRep.InternshipPhaseRepository;
import re.edu.service.InternshipPhaseService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl
        implements InternshipPhaseService {

    private final InternshipPhaseRepository internshipPhaseRepository;

    @Override
    public List<InternshipPhaseResponse> getAllPhases() {

        return internshipPhaseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public InternshipPhaseResponse getPhaseById(Integer phaseId) {

        InternshipPhase phase = internshipPhaseRepository
                .findById(phaseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Internship phase not found"
                        )
                );

        return toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse createPhase(
            InternshipPhaseRequest request
    ) {

        validateDate(request);

        InternshipPhase phase = new InternshipPhase();

        phase.setPhaseName(request.getPhaseName());
        phase.setDescription(request.getDescription());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());

        phase.setCreatedAt(LocalDateTime.now());
        phase.setUpdatedAt(LocalDateTime.now());

        return toResponse(
                internshipPhaseRepository.save(phase)
        );
    }

    @Override
    public InternshipPhaseResponse updatePhase(
            Integer phaseId,
            InternshipPhaseRequest request
    ) {

        validateDate(request);

        InternshipPhase phase = internshipPhaseRepository
                .findById(phaseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Internship phase not found"
                        )
                );

        phase.setPhaseName(request.getPhaseName());
        phase.setDescription(request.getDescription());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());

        phase.setUpdatedAt(LocalDateTime.now());

        return toResponse(
                internshipPhaseRepository.save(phase)
        );
    }

    @Override
    public void deletePhase(Integer phaseId) {

        InternshipPhase phase = internshipPhaseRepository
                .findById(phaseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Internship phase not found"
                        )
                );

        internshipPhaseRepository.delete(phase);
    }

    private InternshipPhaseResponse toResponse(
            InternshipPhase phase
    ) {

        InternshipPhaseResponse response =
                new InternshipPhaseResponse();

        response.setId(phase.getPhaseId());
        response.setPhaseName(phase.getPhaseName());
        response.setDescription(phase.getDescription());
        response.setStartDate(phase.getStartDate());
        response.setEndDate(phase.getEndDate());

        return response;
    }

    private void validateDate(
            InternshipPhaseRequest request
    ) {

        if (request.getEndDate()
                .isBefore(request.getStartDate())) {

            throw new IllegalArgumentException(
                    "End date must be after start date"
            );
        }
    }
}