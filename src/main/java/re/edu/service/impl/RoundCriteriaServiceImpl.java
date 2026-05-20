package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.roundcriteriareq.RoundCriteriaRequest;
import re.edu.model.dto.response.roundcriteriares.RoundCriteriaResponse;
import re.edu.model.entity.AssessmentRound;
import re.edu.model.entity.EvaluationCriteria;
import re.edu.model.entity.RoundCriteria;
import re.edu.repository.assessmentRep.AssessmentRoundRepository;
import re.edu.repository.internshipRep.EvaluationCriteriaRepository;
import re.edu.repository.assessmentRep.RoundCriteriaRepository;
import re.edu.service.RoundCriteriaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundCriteriaServiceImpl implements RoundCriteriaService {

    private final RoundCriteriaRepository roundCriteriaRepository;

    private final AssessmentRoundRepository assessmentRoundRepository;

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    @Override
    public List<RoundCriteriaResponse> getAllRoundCriteria(Integer roundId) {

        List<RoundCriteria> list;

        if (roundId != null) {

            list = roundCriteriaRepository.findByRound_Id(roundId);

        } else {

            list = roundCriteriaRepository.findAll();
        }

        return list.stream().map(this::toResponse).toList();
    }

    @Override
    public RoundCriteriaResponse getRoundCriteriaById(Integer id) {

        RoundCriteria roundCriteria = roundCriteriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí của đợt đánh giá"));

        return toResponse(roundCriteria);
    }

    @Override
    public RoundCriteriaResponse createRoundCriteria(RoundCriteriaRequest request) {

        validateRequest(request);

        boolean exists = roundCriteriaRepository.findByRound_IdAndCriterion_CriterionId(request.getRoundId(), request.getCriterionId()).isPresent();

        if (exists) {

            throw new IllegalArgumentException("Tiêu chí đã tồn tại trong đợt đánh giá này");
        }

        AssessmentRound round = assessmentRoundRepository.findById(request.getRoundId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt đánh giá"));

        EvaluationCriteria criterion = evaluationCriteriaRepository.findById(request.getCriterionId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));

        RoundCriteria roundCriteria = new RoundCriteria();

        roundCriteria.setRound(round);

        roundCriteria.setCriterion(criterion);

        roundCriteria.setWeight(request.getWeight());

        roundCriteria.setCreatedAt(LocalDateTime.now());

        roundCriteria.setUpdatedAt(LocalDateTime.now());

        return toResponse(roundCriteriaRepository.save(roundCriteria));
    }

    @Override
    public RoundCriteriaResponse updateRoundCriteria(Integer id, RoundCriteriaRequest request) {

        RoundCriteria roundCriteria = roundCriteriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí của đợt đánh giá"));

        if (request.getWeight() != null) {

            if (request.getWeight().compareTo(BigDecimal.ZERO) <= 0) {

                throw new IllegalArgumentException("Trọng số phải lớn hơn 0");
            }

            roundCriteria.setWeight(request.getWeight());
        }

        roundCriteria.setUpdatedAt(LocalDateTime.now());

        return toResponse(roundCriteriaRepository.save(roundCriteria));
    }

    @Override
    public void deleteRoundCriteria(Integer id) {

        RoundCriteria roundCriteria = roundCriteriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí của đợt đánh giá"));

        roundCriteriaRepository.delete(roundCriteria);
    }

    private RoundCriteriaResponse toResponse(RoundCriteria rc) {

        RoundCriteriaResponse response = new RoundCriteriaResponse();

        response.setRoundCriterionId(rc.getRoundCriterionId());

        response.setRoundId(rc.getRound().getId());

        response.setRoundName(rc.getRound().getRoundName());

        response.setCriterionId(rc.getCriterion().getCriterionId());

        response.setCriterionName(rc.getCriterion().getCriterionName());

        response.setWeight(rc.getWeight());

        return response;
    }

    private void validateRequest(RoundCriteriaRequest request) {

        if (request.getRoundId() == null) {

            throw new IllegalArgumentException("Mã đợt đánh giá không được để trống");
        }

        if (request.getCriterionId() == null) {

            throw new IllegalArgumentException("Mã tiêu chí đánh giá không được để trống");
        }

        if (request.getWeight() == null) {

            throw new IllegalArgumentException("Trọng số không được để trống");
        }

        if (request.getWeight().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException("Trọng số phải lớn hơn 0");
        }
    }
}