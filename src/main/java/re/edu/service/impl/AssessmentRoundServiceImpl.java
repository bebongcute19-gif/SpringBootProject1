package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.DuplicateResourceException;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.assessmentRoundReq.AssessmentRoundRequest;
import re.edu.model.dto.request.roundcriteriareq.RoundCriteriaRequest;
import re.edu.model.dto.response.assessmentRoundRes.AssessmentRoundResponse;
import re.edu.model.dto.response.assessmentRoundRes.CreateAssessmnet;
import re.edu.model.dto.response.roundcriteriares.CreatAssementRound;
import re.edu.model.dto.response.roundcriteriares.RoundCriteriaResponse;
import re.edu.model.entity.AssessmentRound;
import re.edu.model.entity.EvaluationCriteria;
import re.edu.model.entity.InternshipPhase;
import re.edu.model.entity.RoundCriteria;
import re.edu.repository.assessmentRep.AssessmentResultRepository;
import re.edu.repository.assessmentRep.AssessmentRoundRepository;
import re.edu.repository.assessmentRep.RoundCriteriaRepository;
import re.edu.repository.internshipRep.EvaluationCriteriaRepository;
import re.edu.repository.internshipRep.InternshipPhaseRepository;
import re.edu.service.AssessmentRoundService;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentRoundServiceImpl implements AssessmentRoundService {
    private final RoundCriteriaRepository roundCriteriaRepository;
    private final AssessmentResultRepository assessmentResultRepository;
    private final EvaluationCriteriaRepository evaluationCriteriaRepository;
    private final AssessmentRoundRepository assessmentRoundRepository;

    private final InternshipPhaseRepository internshipPhaseRepository;

    @Override
    public List<AssessmentRoundResponse> getAllRounds(Integer phaseId) {

        List<AssessmentRound> rounds;

        if (phaseId != null) {

            rounds = assessmentRoundRepository.findByPhase_PhaseId(phaseId);

        } else {

            rounds = assessmentRoundRepository.findAll();
        }

        return rounds.stream().map(this::toResponse).toList();
    }

    @Override
    public AssessmentRoundResponse getRoundById(Integer roundId) {

        AssessmentRound round = assessmentRoundRepository.findById(roundId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt đánh giá"));

        return toResponse(round);
    }


    @Override
    public CreateAssessmnet createRound(AssessmentRoundRequest request) {

        validateRequest(request);

        InternshipPhase phase = internshipPhaseRepository.findById(request.getPhaseId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));

        AssessmentRound round = new AssessmentRound();

        round.setPhase(phase);

        round.setRoundName(request.getRoundName());

        round.setStartDate(request.getStartDate());

        round.setEndDate(request.getEndDate());

        round.setDescription(request.getDescription());

        round.setIsActive(request.getIsActive());

        round.setCreatedAt(LocalDateTime.now());

        round.setUpdatedAt(LocalDateTime.now());

        // ===== SAVE ROUND =====
        AssessmentRound savedRound = assessmentRoundRepository.save(round);

        // ===== CHECK DUPLICATE CRITERION =====
        Set<Integer> criterionIds = new HashSet<>();

        // ===== SAVE ROUND CRITERIA =====
        for (RoundCriteriaRequest item : request.getCriteria()) {

            // check duplicate criterion
            if (!criterionIds.add(item.getCriterionId())) {

                throw new DuplicateResourceException("Tiêu chí đánh giá không được trùng");
            }

            EvaluationCriteria criterion = evaluationCriteriaRepository.findById(item.getCriterionId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));

            RoundCriteria roundCriteria = new RoundCriteria();

            roundCriteria.setRound(savedRound);

            roundCriteria.setCriterion(criterion);

            roundCriteria.setWeight(item.getWeight());

            roundCriteriaRepository.save(roundCriteria);
        }

        return toCreateResponse(savedRound);
    }

    @Override
    public AssessmentRoundResponse updateRound(Integer roundId, AssessmentRoundRequest request) {

        AssessmentRound round = assessmentRoundRepository.findById(roundId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt đánh giá"));

        if (request.getPhaseId() != null) {

            InternshipPhase phase = internshipPhaseRepository.findById(request.getPhaseId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));

            round.setPhase(phase);
        }

        if (request.getRoundName() != null && !request.getRoundName().isBlank()) {

            round.setRoundName(request.getRoundName());
        }
        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : round.getStartDate();
        LocalDate endDate = request.getEndDate() != null ? request.getEndDate() : round.getEndDate();
        if(startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        round.setStartDate(startDate);
        round.setEndDate(endDate);
//        if (request.getStartDate() != null) {
//
//            round.setStartDate(request.getStartDate());
//        }
//
//        if (request.getEndDate() != null) {
//
//            round.setEndDate(request.getEndDate());
//        }

        if (request.getDescription() != null) {

            round.setDescription(request.getDescription());
        }

        if (request.getIsActive() != null) {

            round.setIsActive(request.getIsActive());
        }

        round.setUpdatedAt(LocalDateTime.now());

        return toResponse(assessmentRoundRepository.save(round));
    }

    @Override
    public String deleteRound(Integer roundId) {

        AssessmentRound round = assessmentRoundRepository.findById(roundId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt đánh giá"));

        // CHECK ĐÃ CÓ SINH VIÊN ĐƯỢC ĐÁNH GIÁ
        boolean existsResult = assessmentResultRepository.existsByRound_Id(roundId);

        if (existsResult) {

            throw new IllegalStateException("Không thể xóa đợt đánh giá vì đã có sinh viên được đánh giá");
        }

        assessmentRoundRepository.delete(round);

        return "Xóa đợt đánh giá thành công";
    }

    private AssessmentRoundResponse toResponse(AssessmentRound round) {

        AssessmentRoundResponse response = new AssessmentRoundResponse();

        response.setId(round.getId());

        response.setPhaseId(round.getPhase().getPhaseId());

        response.setPhaseName(round.getPhase().getPhaseName());

        response.setRoundName(round.getRoundName());

        response.setStartDate(round.getStartDate());

        response.setEndDate(round.getEndDate());

        response.setDescription(round.getDescription());

        response.setIsActive(round.getIsActive());

        return response;
    }

    private CreateAssessmnet toCreateResponse(AssessmentRound round) {

        CreateAssessmnet response = new CreateAssessmnet();

        response.setId(round.getId());

        response.setPhaseId(round.getPhase().getPhaseId());

        response.setPhaseName(round.getPhase().getPhaseName());

        response.setRoundName(round.getRoundName());

        response.setDescription(round.getDescription());

        response.setStartDate(round.getStartDate());

        response.setEndDate(round.getEndDate());

        response.setIsActive(round.getIsActive());

        // ===== GET ROUND CRITERIA =====
        List<CreatAssementRound> criteriaResponses = roundCriteriaRepository.findByRound_Id(round.getId()).stream().map(item -> {

            CreatAssementRound res = new CreatAssementRound();

            res.setCriterionId(item.getCriterion().getCriterionId());

            res.setCriterionName(item.getCriterion().getCriterionName());

            res.setWeight(item.getWeight());

            return res;
        }).toList();

        response.setCriteria(criteriaResponses);

        return response;
    }

    private void validateRequest(AssessmentRoundRequest request) {

        if (request.getPhaseId() == null) {

            throw new IllegalArgumentException("Mã giai đoạn thực tập không được để trống");
        }

        if (request.getRoundName() == null || request.getRoundName().isBlank()) {

            throw new IllegalArgumentException("Tên đợt đánh giá không được để trống");
        }

        if (request.getStartDate() == null) {

            throw new IllegalArgumentException("Ngày bắt đầu không được để trống");
        }

        if (request.getEndDate() == null) {

            throw new IllegalArgumentException("Ngày kết thúc không được để trống");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {

            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
    }
}