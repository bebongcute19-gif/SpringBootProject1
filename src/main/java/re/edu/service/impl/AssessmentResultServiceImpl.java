package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.assessmentresultreq.AssessmentResultRequest;
import re.edu.model.dto.request.assessmentresultreq.AssessmentResultUpdateRequest;
import re.edu.model.dto.response.assessmentresultres.AssessmentResultResponse;
import re.edu.model.entity.*;
import re.edu.repository.assessmentRep.AssessmentResultRepository;
import re.edu.repository.assessmentRep.AssessmentRoundRepository;
import re.edu.repository.assessmentRep.InternshipAssignmentRepository;
import re.edu.repository.internshipRep.EvaluationCriteriaRepository;
import re.edu.security.userDetail.CustomUserDetails;
import re.edu.service.AssessmentResultService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentResultServiceImpl
        implements AssessmentResultService {

    private final AssessmentResultRepository resultRepository;

    private final InternshipAssignmentRepository
            assignmentRepository;

    private final AssessmentRoundRepository
            roundRepository;

    private final EvaluationCriteriaRepository
            criteriaRepository;

    @Override
    public List<AssessmentResultResponse>
    getAllResults(Integer assignmentId) {

        List<AssessmentResult> results;

        if (assignmentId != null) {

            results =
                    resultRepository
                            .findByAssignment_AssignmentId(
                                    assignmentId
                            );

        } else {

            results = resultRepository.findAll();
        }

        return results.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AssessmentResultResponse createResult(
            AssessmentResultRequest request
    ) {

        InternshipAssignment assignment =
                assignmentRepository.findById(
                        request.getAssignmentId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Assignment not found"
                        )
                );

        AssessmentRound round =
                roundRepository.findById(
                        request.getRoundId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Round not found"
                        )
                );

        EvaluationCriteria criterion =
                criteriaRepository.findById(
                        request.getCriterionId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Criterion not found"
                        )
                );

        User currentUser = getCurrentUser();

        // ===== CHỈ MENTOR ĐƯỢC PHÂN CÔNG =====
        if (!assignment.getMentor()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "Bạn không được phân công sinh viên này"
            );
        }

        // ===== DUPLICATE =====
        boolean exists =
                resultRepository
                        .findByAssignment_AssignmentIdAndRound_IdAndCriterion_CriterionId(
                                request.getAssignmentId(),
                                request.getRoundId(),
                                request.getCriterionId()
                        )
                        .isPresent();

        if (exists) {

            throw new IllegalArgumentException(
                    "Assessment result already exists"
            );
        }

        AssessmentResult result =
                new AssessmentResult();

        result.setAssignment(assignment);

        result.setRound(round);

        result.setCriterion(criterion);

        result.setScore(request.getScore());

        result.setComments(
                request.getComments()
        );

        result.setEvaluatedBy(currentUser);

        result.setEvaluationDate(
                LocalDateTime.now()
        );

        return toResponse(
                resultRepository.save(result)
        );
    }

    @Override
    public AssessmentResultResponse updateResult(
            Integer resultId,
            AssessmentResultUpdateRequest request
    ) {

        AssessmentResult result =
                resultRepository.findById(resultId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assessment result not found"
                                )
                        );

        User currentUser =
                getCurrentUser();

        // ===== CHỈ SỬA KẾT QUẢ CỦA CHÍNH MÌNH =====
        if (!result.getEvaluatedBy()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "Bạn không thể sửa kết quả của mentor khác"
            );
        }

        result.setScore(
                request.getScore()
        );

        result.setComments(
                request.getComments()
        );

        return toResponse(
                resultRepository.save(result)
        );
    }

    private User getCurrentUser() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        auth.getPrincipal();

        return userDetails.getUser();
    }

    private AssessmentResultResponse toResponse(
            AssessmentResult result
    ) {

        AssessmentResultResponse response =
                new AssessmentResultResponse();

        response.setResultId(
                result.getResultId()
        );

        response.setAssignmentId(
                result.getAssignment()
                        .getAssignmentId()
        );

        response.setStudentId(
                result.getAssignment()
                        .getStudent()
                        .getId()
        );

        response.setStudentName(
                result.getAssignment()
                        .getStudent()
                        .getUser()
                        .getFullName()
        );

        response.setMentorId(
                result.getAssignment()
                        .getMentor()
                        .getId()
        );

        response.setMentorName(
                result.getAssignment()
                        .getMentor()
                        .getUser()
                        .getFullName()
        );

        response.setRoundId(
                result.getRound()
                        .getId()
        );

        response.setRoundName(
                result.getRound()
                        .getRoundName()
        );

        response.setCriterionId(
                result.getCriterion()
                        .getCriterionId()
        );

        response.setCriterionName(
                result.getCriterion()
                        .getCriterionName()
        );

        response.setScore(
                result.getScore()
        );

        response.setComments(
                result.getComments()
        );

        response.setEvaluatedById(
                result.getEvaluatedBy()
                        .getId()
        );

        response.setEvaluatedByName(
                result.getEvaluatedBy()
                        .getFullName()
        );

        response.setEvaluationDate(
                result.getEvaluationDate()
        );

        return response;
    }
}