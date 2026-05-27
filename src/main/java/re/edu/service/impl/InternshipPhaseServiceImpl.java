package re.edu.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.internshipPhaseReq.InternshipPhaseRequest;
import re.edu.model.dto.request.internshipPhaseReq.UpdateInternshipPhase;
import re.edu.model.dto.response.internshipPhaseRes.InternshipPhaseResponse;
import re.edu.model.entity.InternshipPhase;
import re.edu.repository.internshipRep.InternshipPhaseRepository;
import re.edu.service.InternshipPhaseService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements InternshipPhaseService {

    private final InternshipPhaseRepository internshipPhaseRepository;

    @Override
    public List<InternshipPhaseResponse> getAllPhases() {

        return internshipPhaseRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public InternshipPhaseResponse getPhaseById(Integer phaseId) {

        InternshipPhase phase = internshipPhaseRepository.findById(phaseId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));

        return toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse createPhase(InternshipPhaseRequest request) {

        validateDate(request);

        InternshipPhase phase = new InternshipPhase();

        phase.setPhaseName(request.getPhaseName());
        phase.setDescription(request.getDescription());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());

        phase.setCreatedAt(LocalDateTime.now());
        phase.setUpdatedAt(LocalDateTime.now());

        return toResponse(internshipPhaseRepository.save(phase));
    }

    @Override
    public InternshipPhaseResponse updatePhase(
            Integer phaseId,
            UpdateInternshipPhase request
    ) {

        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Không tìm thấy giai đoạn thực tập"
                        )
                );

        // update phaseName
        if (request.getPhaseName() != null
                && !request.getPhaseName().isBlank()) {

            phase.setPhaseName(request.getPhaseName());
        }

        // update description
        if (request.getDescription() != null) {

            phase.setDescription(request.getDescription());
        }

        // update startDate
        if (request.getStartDate() != null) {

            phase.setStartDate(request.getStartDate());
        }

        // update endDate
        if (request.getEndDate() != null) {

            phase.setEndDate(request.getEndDate());
        }

        // validate date sau khi update
        if (phase.getEndDate().isBefore(phase.getStartDate())) {

            throw new IllegalArgumentException(
                    "Ngày kết thúc phải sau ngày bắt đầu"
            );
        }

        phase.setUpdatedAt(LocalDateTime.now());

        return toResponse(
                internshipPhaseRepository.save(phase)
        );
    }

    @Override
    public void deletePhase(Integer phaseId) {

        InternshipPhase phase = internshipPhaseRepository.findById(phaseId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));

        internshipPhaseRepository.delete(phase);
    }

    private InternshipPhaseResponse toResponse(InternshipPhase phase) {

        InternshipPhaseResponse response = new InternshipPhaseResponse();

        response.setId(phase.getPhaseId());
        response.setPhaseName(phase.getPhaseName());
        response.setDescription(phase.getDescription());
        response.setStartDate(phase.getStartDate());
        response.setEndDate(phase.getEndDate());

        return response;
    }

    private void validateDate(InternshipPhaseRequest request) {

        if (request.getEndDate().isBefore(request.getStartDate())) {

            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
    }
}