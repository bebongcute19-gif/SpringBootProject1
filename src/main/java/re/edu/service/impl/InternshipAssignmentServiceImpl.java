package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.internshipassignmentreq.InternshipAssignmentRequest;
import re.edu.model.dto.request.internshipassignmentreq.UpdateAssignmentStatusRequest;
import re.edu.model.dto.response.internshipassignmentres.InternshipAssignmentResponse;
import re.edu.model.entity.InternshipAssignment;
import re.edu.model.entity.InternshipPhase;
import re.edu.model.entity.Mentor;
import re.edu.model.entity.Student;
import re.edu.model.enums.AssignmentStatus;
import re.edu.repository.assessmentRep.InternshipAssignmentRepository;
import re.edu.repository.internshipRep.InternshipPhaseRepository;
import re.edu.repository.userRep.MentorRepository;
import re.edu.repository.userRep.StudentRepository;
import re.edu.service.InternshipAssignmentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipAssignmentServiceImpl
        implements InternshipAssignmentService {

    private final InternshipAssignmentRepository
            assignmentRepository;

    private final StudentRepository
            studentRepository;

    private final MentorRepository
            mentorRepository;

    private final InternshipPhaseRepository
            phaseRepository;

    @Override
    public List<InternshipAssignmentResponse>
    getAllAssignments() {

        return assignmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public InternshipAssignmentResponse
    getAssignmentById(Integer assignmentId) {

        InternshipAssignment assignment =
                assignmentRepository
                        .findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assignment not found"
                                )
                        );

        return toResponse(assignment);
    }

    @Override
    public InternshipAssignmentResponse
    createAssignment(
            InternshipAssignmentRequest request
    ) {

        validateRequest(request);

        boolean exists =
                assignmentRepository
                        .findByStudent_IdAndPhase_PhaseId(
                                request.getStudentId(),
                                request.getPhaseId()
                        )
                        .isPresent();
        if (exists) {

            throw new IllegalArgumentException(
                    "Student already assigned in this phase"
            );
        }

        Student student =
                studentRepository
                        .findById(request.getStudentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found"
                                )
                        );

        Mentor mentor =
                mentorRepository
                        .findById(request.getMentorId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Mentor not found"
                                )
                        );

        InternshipPhase phase =
                phaseRepository
                        .findById(request.getPhaseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Internship phase not found"
                                )
                        );

        InternshipAssignment assignment =
                new InternshipAssignment();

        assignment.setStudent(student);

        assignment.setMentor(mentor);

        assignment.setPhase(phase);

        assignment.setAssignedDate(
                LocalDateTime.now()
        );

        assignment.setStatus(
                AssignmentStatus.PENDING
        );

        assignment.setCreatedAt(
                LocalDateTime.now()
        );

        assignment.setUpdatedAt(
                LocalDateTime.now()
        );

        return toResponse(
                assignmentRepository.save(assignment)
        );
    }

    @Override
    public InternshipAssignmentResponse
    updateAssignmentStatus(
            Integer assignmentId,
            UpdateAssignmentStatusRequest request
    ) {

        InternshipAssignment assignment =
                assignmentRepository
                        .findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assignment not found"
                                )
                        );

        if (request.getStatus() == null) {

            throw new IllegalArgumentException(
                    "Status is required"
            );
        }

        assignment.setStatus(
                request.getStatus()
        );

        assignment.setUpdatedAt(
                LocalDateTime.now()
        );

        return toResponse(
                assignmentRepository.save(assignment)
        );
    }

    private InternshipAssignmentResponse
    toResponse(InternshipAssignment assignment) {

        InternshipAssignmentResponse response =
                new InternshipAssignmentResponse();

        response.setAssignmentId(
                assignment.getAssignmentId()
        );

        response.setStudentId(
                assignment.getStudent()
                        .getId()
        );

        response.setStudentName(
                assignment.getStudent()
                        .getUser()
                        .getFullName()
        );

        response.setMentorId(
                assignment.getMentor()
                        .getId()
        );

        response.setMentorName(
                assignment.getMentor()
                        .getUser()
                        .getFullName()
        );

        response.setPhaseId(
                assignment.getPhase()
                        .getPhaseId()
        );

        response.setPhaseName(
                assignment.getPhase()
                        .getPhaseName()
        );

        response.setAssignedDate(
                assignment.getAssignedDate()
        );

        response.setStatus(
                assignment.getStatus()
        );

        return response;
    }

    private void validateRequest(
            InternshipAssignmentRequest request
    ) {

        if (request.getStudentId() == null) {

            throw new IllegalArgumentException(
                    "Student id is required"
            );
        }

        if (request.getMentorId() == null) {

            throw new IllegalArgumentException(
                    "Mentor id is required"
            );
        }

        if (request.getPhaseId() == null) {

            throw new IllegalArgumentException(
                    "Phase id is required"
            );
        }
    }
}