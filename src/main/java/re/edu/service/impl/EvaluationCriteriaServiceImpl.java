package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.DuplicateResourceException;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.evaluationCriteriaReq.EvaluationCriteriaRequest;
import re.edu.model.dto.response.evaluationCriteriaRes.EvaluationCriteriaResponse;
import re.edu.model.entity.EvaluationCriteria;

import re.edu.repository.internshipRep.EvaluationCriteriaRepository;
import re.edu.service.EvaluationCriteriaService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    @Override
    public List<EvaluationCriteriaResponse> getAllCriteria() {

        return evaluationCriteriaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public EvaluationCriteriaResponse getCriterionById(Integer criterionId) {

        EvaluationCriteria criterion = evaluationCriteriaRepository.findById(criterionId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));

        return toResponse(criterion);
    }

    @Override
    public EvaluationCriteriaResponse createCriterion(EvaluationCriteriaRequest request) {

        validateCreateRequest(request);

        if (evaluationCriteriaRepository.existsByCriterionName(request.getCriterionName())) {

            throw new DuplicateResourceException("Tên tiêu chí đánh giá đã tồn tại");
        }

        EvaluationCriteria criterion = new EvaluationCriteria();

        criterion.setCriterionName(request.getCriterionName());

        criterion.setDescription(request.getDescription());

        criterion.setMaxScore(request.getMaxScore());

        criterion.setCreatedAt(LocalDateTime.now());
        criterion.setUpdatedAt(LocalDateTime.now());

        return toResponse(evaluationCriteriaRepository.save(criterion));
    }

    @Override
    public EvaluationCriteriaResponse updateCriterion(Integer criterionId, EvaluationCriteriaRequest request) {

        EvaluationCriteria criterion = evaluationCriteriaRepository.findById(criterionId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));

        if (request.getCriterionName() != null && !request.getCriterionName().isBlank()) {

            criterion.setCriterionName(request.getCriterionName());
        }

        if (request.getDescription() != null) {

            criterion.setDescription(request.getDescription());
        }

        if (request.getMaxScore() != null) {

            if (request.getMaxScore().doubleValue() <= 0) {

                throw new IllegalArgumentException("Điểm tối đa phải lớn hơn 0");
            }

            criterion.setMaxScore(request.getMaxScore());
        }

        criterion.setUpdatedAt(LocalDateTime.now());

        return toResponse(evaluationCriteriaRepository.save(criterion));
    }

    @Override
    public void deleteCriterion(Integer criterionId) {

        EvaluationCriteria criterion = evaluationCriteriaRepository.findById(criterionId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));

        evaluationCriteriaRepository.delete(criterion);
    }

    private EvaluationCriteriaResponse toResponse(EvaluationCriteria criterion) {

        EvaluationCriteriaResponse response = new EvaluationCriteriaResponse();

        response.setId(criterion.getCriterionId());

        response.setCriterionName(criterion.getCriterionName());

        response.setDescription(criterion.getDescription());

        response.setMaxScore(criterion.getMaxScore());

        return response;
    }

    private void validateCreateRequest(EvaluationCriteriaRequest request) {

        if (request.getCriterionName() == null || request.getCriterionName().isBlank()) {

            throw new IllegalArgumentException("Tên tiêu chí đánh giá không được để trống");
        }

        if (request.getMaxScore() == null) {

            throw new IllegalArgumentException("Điểm tối đa không được để trống");
        }

        if (request.getMaxScore().doubleValue() <= 0) {

            throw new IllegalArgumentException("Điểm tối đa phải lớn hơn 0");
        }
    }
}